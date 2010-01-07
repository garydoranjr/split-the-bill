package com.appspot.splitbill.client.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.EmailFrequency;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.GroupThumbnail;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.event.EventBus;
import com.appspot.splitbill.client.event.GroupThumbsUpdateEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent;
import com.appspot.splitbill.client.event.NotificationEvent;
import com.appspot.splitbill.client.event.GroupUpdateEvent.GroupUpdateType;
import com.appspot.splitbill.client.event.NotificationEvent.NotificationEventType;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GroupManager {

	private static final boolean DEFAULT_CACHE_POLICY = true;
	
	private EventBus eventBus;
	
	private GroupServiceAsync groupService;
	private EARServiceAsync earService;
	private SettingsServiceAsync setService;
	private LoginManager loginManager;
	
	private ArrayList<GroupThumbnail> cachedThumbs = null;
	private HashMap<Long, Group> groups = new HashMap<Long, Group>();
	
	@Inject
	public GroupManager(GroupServiceAsync groupService,
						EARServiceAsync earService,
						SettingsServiceAsync setService,
						LoginManager loginManager,
						EventBus eventBus){
		this.eventBus = eventBus;
		this.groupService = groupService;
		this.earService = earService;
		this.setService = setService;
		this.loginManager = loginManager;
	}
	
	public void updateThumbs(){
		updateThumbs(DEFAULT_CACHE_POLICY);
	}
	public void updateThumbs(final boolean useCached){
		if(!useCached || cachedThumbs == null){
			incrAsync();
			groupService.getGroupThumbs(new AACB<List<GroupThumbnail>>(){
				@Override
				public void processResult(List<GroupThumbnail> result) {
					cachedThumbs = new ArrayList<GroupThumbnail>(result);
					thumbsUpdated();
				}
			});
		}else{
			thumbsUpdated();
		}
	}
	private void thumbsUpdated(){
		eventBus.fireEvent(new GroupThumbsUpdateEvent());
	}
	public List<GroupThumbnail> getGroupThumbs(){
		return cachedThumbs;
	}
	
	public void updateGroup(long groupID){
		updateGroup(groupID, DEFAULT_CACHE_POLICY);
	}
	public void updateGroup(long groupID, boolean useCached){
		Group group = groups.get(groupID);
		if(!useCached || group == null){
			incrAsync();
			groupService.getGroup(groupID, new AACB<Group>(){
				@Override
				public void processResult(Group result) {
					groups.put(result.getId(), result);
					groupUpdated(GroupUpdateType.GROUP, result);
				}
			});
		}else{
			groupUpdated(GroupUpdateType.GROUP, group);
		}
	}
	public void groupUpdated(GroupUpdateType type, Group group){
		eventBus.fireEvent(new GroupUpdateEvent(type, group));
	}
	public void groupUpdated(Group group, Bill bill){
		eventBus.fireEvent(new GroupUpdateEvent(GroupUpdateType.GETS, group, bill));
	}
	
	public void addGroup(GroupThumbnail group){
		incrAsync();
		groupService.addGroup(group, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				updateThumbs(false);
			}
		});
	}
	
	public void removeGroup(long groupID){
		incrAsync();
		groupService.removeGroup(groupID, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				updateThumbs(false);
			}
		});
	}
	
	public void editGroup(final GroupThumbnail group){
		incrAsync();
		groupService.editGroup(group, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				updateThumbs(false);
				Group changedGroup = groups.get(group.getId());
				if(changedGroup != null){
					changedGroup.setName(group.getName());
					changedGroup.setDescription(group.getDescription());
					changedGroup.setPassword(group.getPassword());
					groupUpdated(GroupUpdateType.GROUP, changedGroup);
				}
			}
		});
	}
	
	public boolean removePerson(final long groupID, final long personID){
		Group group = groups.get(groupID);
		ClientUser user = loginManager.getInfo().getUserInfo();
		if(group == null){
			return false;
		}
		Person loggedInPerson = group.getPerson(user);
		if(loggedInPerson.getID() == personID){
			eventBus.fireEvent(new NotificationEvent(NotificationEventType.NOTIFY, "You cannot delete yourself.", 5000));
			return false;
		}else{
			incrAsync();
			earService.removePerson(groupID, personID, new AACB<Void>(){
				@Override
				public void processResult(Void result) {
					Group changedGroup = groups.get(groupID);
					changedGroup.removePerson(personID);
					groupUpdated(GroupUpdateType.PERSONS, changedGroup);
				}
			});
			return true;
		}
	}
	
	public void addPerson(final long groupID, final Person toAdd){
		incrAsync();
		earService.addPerson(groupID, toAdd, new AACB<Person>(){
			@Override
			public void processResult(Person result) {
				Group changedGroup = groups.get(groupID);
				changedGroup.addPerson(result);
				groupUpdated(GroupUpdateType.PERSONS, changedGroup);
			}
		});
	}
	
	public void editPerson(final long groupID, final Person toEdit){
		incrAsync();
		earService.editPerson(groupID, toEdit, new AACB<ClientUser>(){
			@Override
			public void processResult(ClientUser result) {
				Group changedGroup = groups.get(groupID);
				Person changdPerson = changedGroup.getPerson(toEdit.getID());
				changdPerson.setName(toEdit.getName());
				changdPerson.setType(toEdit.getType());
				changdPerson.setUser(result);
				changdPerson.setWeight(toEdit.getWeight());
				changedGroup.updatePercentages();
				groupUpdated(GroupUpdateType.PERSONS, changedGroup);
			}
		});
	}
	
	public void removePay(final long groupID, final long payID){
		incrAsync();
		earService.removePays(groupID, payID, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				changedGroup.removePays(payID);
				groupUpdated(GroupUpdateType.PAYS, changedGroup);
			}
		});
	}
	
	public void addPay(final long groupID, final Pays toAdd){
		incrAsync();
		earService.addPays(groupID, toAdd, new AACB<Pays>(){
			@Override
			public void processResult(Pays result) {
				Group changedGroup = groups.get(groupID);
				changedGroup.addPays(result);
				groupUpdated(GroupUpdateType.PAYS, changedGroup);
			}
		});
	}
	
	public void editPay(final long groupID, final Pays toEdit){
		incrAsync();
		earService.editPays(groupID, toEdit, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				Pays p = changedGroup.getPays(toEdit.getID());
				p.setPayerID(toEdit.getPayerID());
				p.setPayeeID(toEdit.getPayeeID());
				p.setAmount(toEdit.getAmount());
				p.setDescription(toEdit.getDescription());
				p.setDate(toEdit.getDate());
				groupUpdated(GroupUpdateType.PAYS, changedGroup);
			}
		});
	}
	
	public void removeBill(final long groupID, final long billID){
		incrAsync();
		earService.removeBill(groupID, billID, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				changedGroup.removeBill(billID);
				groupUpdated(GroupUpdateType.BILLS, changedGroup);
			}
		});
	}
	
	public void addBill(final long groupID, final Bill toAdd){
		incrAsync();
		earService.addBill(groupID, toAdd, new AACB<Bill>(){
			@Override
			public void processResult(Bill result) {
				Group changedGroup = groups.get(groupID);
				changedGroup.addBill(result);
				groupUpdated(GroupUpdateType.BILLS, changedGroup);
			}
		});
	}
	
	public void editBill(final long groupID, final Bill toEdit){
		incrAsync();
		earService.editBill(groupID, toEdit, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				Bill b = changedGroup.getBill(toEdit.getID());
				b.setAmount(toEdit.getAmount());
				b.setDate(toEdit.getDate());
				b.setPayee(toEdit.getPayee());
				b.setDescription(toEdit.getDescription());
				b.setBuyerID(toEdit.getBuyerID());
				groupUpdated(GroupUpdateType.BILLS, changedGroup);
			}
		});
	}
	
	public void removeGets(final long groupID, final long billID, final long getsID){
		incrAsync();
		earService.removeGets(groupID, billID, getsID, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				Bill changedBill = changedGroup.getBill(billID);
				changedBill.removeGets(getsID);
				groupUpdated(changedGroup, changedBill);
			}
		});
	}
	
	public void addGet(final long groupID, final long billID, final Gets toAdd){
		incrAsync();
		earService.addGets(groupID, billID, toAdd, new AACB<Gets>(){
			@Override
			public void processResult(Gets result) {
				Group changedGroup = groups.get(groupID);
				Bill changedBill = changedGroup.getBill(billID);
				changedBill.addGets(result);
				groupUpdated(changedGroup, changedBill);
			}
		});
	}
	
	public void setEmailFreq(final long groupID, final long personID, final EmailFrequency newFreq){
		incrAsync();
		setService.setEmailFreq(groupID, personID, newFreq, new AACB<Void>(){
			@Override
			public void processResult(Void result) {
				Group changedGroup = groups.get(groupID);
				Person changedPerson = changedGroup.getPerson(personID);
				changedPerson.setEmailFreq(newFreq);
				// No event needs to be fired for this, I think.
			}
		});
	}
	
	private void incrAsync(){
		eventBus.fireEvent(new NotificationEvent(NotificationEventType.LOADING));
	}
	private void decrAsync(){
		eventBus.fireEvent(new NotificationEvent(NotificationEventType.DONE));
	}
	
	private void handleThrowable(Throwable t){
		eventBus.fireEvent(new NotificationEvent(NotificationEventType.ERROR));
		t.printStackTrace();
	}
	
	private abstract class AACB<T> implements AsyncCallback<T> {
		@Override
		public void onFailure(Throwable caught) {
			decrAsync();
			handleThrowable(caught);
		}
		@Override
		public final void onSuccess(T result){
			decrAsync();
			processResult(result);
		}
		public abstract void processResult(T result);
	}
	
}
