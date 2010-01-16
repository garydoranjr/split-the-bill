package com.appspot.splitbill.server;

import javax.jdo.PersistenceManager;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.rpc.EARService;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.rpc.ServiceException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EARServiceImpl  extends RemoteServiceServlet implements EARService{

	private static final long serialVersionUID = -8386037269706502286L;

	@Override
	public ClientUser editPerson(long groupID, Person person) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), person.getID()).getKey();
			PersonJDO p = pm.getObjectById(PersonJDO.class, key);
			ServiceStatic.checkAdmin(user, p.getGroup());
			p.setName(person.getName());
			p.setType(person.getType());
			p.setWeight(person.getWeight());
			ClientUser cUser = person.getUser();
			if(cUser != null){
				User pUser = new User(cUser.getEmail(),"gmail.com");
				p.setUser(pUser);
			}else{
				p.setUser(null);
				return null;
			}
			User persistUser = p.getUser();
			ClientUser retVal = new ClientUser(persistUser.getUserId(),
												persistUser.getEmail(),
												persistUser.getNickname());
			return retVal;
		}finally{
			pm.close();
		}
	}

	@Override
	public void removePerson(long groupID, long personID) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), personID).getKey();
			PersonJDO person = pm.getObjectById(PersonJDO.class, key);
			ServiceStatic.checkAdmin(user, person.getGroup());
			ServiceStatic.checkNotThisUser(user, person.getGroup(), personID);
			pm.deletePersistent(person);
		}finally{
			pm.close();
		}
	}

	@Override
	public Person addPerson(long groupID, Person person) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkAdmin(user, group);
			ClientUser cUser = person.getUser();
			User pUser = null;
			if(cUser != null){
				pUser = new User(cUser.getEmail(),"gmail.com");
			}
			PersonJDO persistPerson = new PersonJDO(group,
					person.getName(),
					pUser,
					person.getWeight(),
					person.getType(),
					person.getEmailFreq());
			group.addPerson(persistPerson);
			pm.close();
			Person retVal = new Person(persistPerson.getKey().getId());
			retVal.setName(persistPerson.getName());
			retVal.setType(persistPerson.getType());
			retVal.setWeight(persistPerson.getWeight());
			User persistUser = persistPerson.getUser();
			if(persistUser == null){
				retVal.setUser(null);
			}else{
				ClientUser retUser = new ClientUser(persistUser.getUserId(),
						persistUser.getEmail(), persistUser.getNickname());
				retVal.setUser(retUser);
			}
			return retVal;
		}finally{
			if(!pm.isClosed()){
				pm.close();
			}
		}
	}

	@Override
	public Bill addBill(long groupID, Bill bill) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkAdmin(user, group);
			Key buyerKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID)
				.addChild(PersonJDO.class.getSimpleName(), bill.getBuyerID()).getKey();
			BillJDO persistBill = new BillJDO(group,
					buyerKey,
					bill.getDate(),
					bill.getPayee(),
					bill.getDescription(),
					bill.getAmount());
			group.addBill(persistBill);
			
			// Add Gets
			for(Gets gets : bill.getGets()){
				Key personKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), gets.getPersonID()).getKey();
				GetsJDO persistGets = new GetsJDO(persistBill,
						personKey,
						gets.getAmount(),
						gets.getDescription());
				persistBill.addGets(persistGets);
			}
			
			pm.close();
			Bill retVal = new Bill(persistBill.getKey().getId());
			retVal.setAmount(persistBill.getAmount());
			retVal.setBuyerID(persistBill.getBuyer().getId());
			retVal.setDate(persistBill.getDate());
			retVal.setDescription(persistBill.getDescription());
			retVal.setPayee(persistBill.getPayee());
			
			for(GetsJDO persistGets : persistBill.getGets()){
				Gets gets = new Gets(persistGets.getKey().getId());
				gets.setAmount(persistGets.getAmount());
				gets.setDescription(persistGets.getDescription());
				gets.setPersonID(persistGets.getPerson().getId());
				retVal.addGets(gets);
			}
			
			return retVal;
		}finally{
			if(!pm.isClosed()){
				pm.close();
			}
		}
	}

	@Override
	public void removeBill(long groupID, long billID) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(BillJDO.class.getSimpleName(), billID).getKey();
			BillJDO bill = pm.getObjectById(BillJDO.class, key);
			ServiceStatic.checkAdmin(user, bill.getGroup());
			pm.deletePersistent(bill);
		}finally{
			pm.close();
		}
	}

	@Override
	public void editBill(long groupID, Bill bill) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(BillJDO.class.getSimpleName(), bill.getID()).getKey();
			BillJDO b = pm.getObjectById(BillJDO.class, key);
			ServiceStatic.checkAdmin(user, b.getGroup());
			b.setAmount(bill.getAmount());
			b.setDate(bill.getDate());
			b.setDescription(bill.getDescription());
			b.setPayee(bill.getPayee());
			Key buyerKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), bill.getBuyerID()).getKey();
			b.setBuyer(buyerKey);
		}finally{
			pm.close();
		}
	}

	@Override
	public Gets addGets(long groupID, long billID, Gets gets)
			throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key billKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(BillJDO.class.getSimpleName(), billID).getKey();
			BillJDO bill = pm.getObjectById(BillJDO.class, billKey);
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkAdmin(user, group);
			Key personKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), gets.getPersonID()).getKey();
			GetsJDO persistGets = new GetsJDO(bill,
					personKey,
					gets.getAmount(),
					gets.getDescription());
			bill.addGets(persistGets);
			pm.close();
			Gets retVal = new Gets(persistGets.getKey().getId());
			retVal.setAmount(persistGets.getAmount());
			retVal.setDescription(persistGets.getDescription());
			retVal.setPersonID(persistGets.getPerson().getId());
			return retVal;
		}finally{
			if(!pm.isClosed()){
				pm.close();
			}
		}
	}

	@Override
	public void removeGets(long groupID, long billID, long getsID)
			throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			ServiceStatic.checkAdmin(user, pm.getObjectById(GroupJDO.class, groupID));
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID)
			.addChild(BillJDO.class.getSimpleName(), billID)
			.addChild(GetsJDO.class.getSimpleName(),getsID).getKey();
			GetsJDO gets = pm.getObjectById(GetsJDO.class, key);
			pm.deletePersistent(gets);
		}finally{
			pm.close();
		}
	}

	@Override
	public Pays addPays(long groupID, Pays pays) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			GroupJDO group = pm.getObjectById(GroupJDO.class, groupID);
			ServiceStatic.checkAdmin(user, group);
			Key payerKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID)
				.addChild(PersonJDO.class.getSimpleName(), pays.getPayerID()).getKey();
			Key payeeKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID)
				.addChild(PersonJDO.class.getSimpleName(), pays.getPayeeID()).getKey();
			PaysJDO persistPays = new PaysJDO(group,payerKey, payeeKey,pays.getAmount(),pays.getDescription(),pays.getDate());
			group.addPayment(persistPays);
			pm.close();
			Pays retVal = new Pays(persistPays.getKey().getId());
			retVal.setPayerID(persistPays.getPayer().getId());
			retVal.setPayeeID(persistPays.getPayee().getId());
			retVal.setAmount(persistPays.getAmount());
			retVal.setDescription(persistPays.getDescription());
			retVal.setDate(persistPays.getDate());
			return retVal;
		}finally{
			if(!pm.isClosed()){
				pm.close();
			}
		}
	}

	@Override
	public void editPays(long groupID, Pays pays) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PaysJDO.class.getSimpleName(), pays.getID()).getKey();
			PaysJDO p = pm.getObjectById(PaysJDO.class, key);
			ServiceStatic.checkAdmin(user, p.getGroup());
			Key payerKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), pays.getPayerID()).getKey();
			Key payeeKey = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PersonJDO.class.getSimpleName(), pays.getPayeeID()).getKey();
			p.setPayer(payerKey);
			p.setPayee(payeeKey);
			p.setAmount(pays.getAmount());
			p.setDescription(pays.getDescription());
			p.setDate(pays.getDate());
		}finally{
			pm.close();
		}
	}

	@Override
	public void removePays(long groupID, long paysID) throws ServiceException {
		User user = ServiceStatic.getUser();
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Key key = new KeyFactory.Builder(GroupJDO.class.getSimpleName(), groupID).addChild(PaysJDO.class.getSimpleName(), paysID).getKey();
			PaysJDO pays = pm.getObjectById(PaysJDO.class, key);
			ServiceStatic.checkAdmin(user, pays.getGroup());
			pm.deletePersistent(pays);
		}finally{
			pm.close();
		}
	}

}
