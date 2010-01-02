package com.appspot.splitbill.server;

import java.text.NumberFormat;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Group;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.PersonType;
import com.appspot.splitbill.client.rpc.ServiceException;
import com.appspot.splitbill.client.rpc.ServiceException.ExceptionType;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class ServiceStatic {

	public static String formatAmount(double amount){
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(amount);
	}
	
	public static User getUser() throws ServiceException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			throw new ServiceException(ExceptionType.NOT_LOGGED_IN);
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public static void checkThisUser(User user, GroupJDO group, long personID) throws ServiceException{
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Query q = pm.newQuery(PersonJDO.class,"user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<PersonJDO> persons = (List<PersonJDO>) q.execute(user);
			for(PersonJDO person : persons){
				if(person.getKey().getId() == personID &&
						group.getId() == person.getGroup().getId()){
					return;
				}
			}
			throw new ServiceException(ExceptionType.UNAUTHORIZED);
		}finally{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void checkAdmin(User user, GroupJDO group) throws ServiceException {
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Query q = pm.newQuery(PersonJDO.class,"user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<PersonJDO> persons = (List<PersonJDO>) q.execute(user);
			for(PersonJDO person : persons){
				if(person.getType() == PersonType.ADMIN &&
						group.getId() == person.getGroup().getId()){
					return;
				}
			}
			throw new ServiceException(ExceptionType.UNAUTHORIZED);
		}finally{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void checkMember(User user, GroupJDO group) throws ServiceException {
		PersistenceManager pm = ServiceStatic.getPersistenceManager();
		try{
			Query q = pm.newQuery(PersonJDO.class,"user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<PersonJDO> persons = (List<PersonJDO>) q.execute(user);
			for(PersonJDO person : persons){
				if(group.getId() == person.getGroup().getId()){
					return;
				}
			}
			throw new ServiceException(ExceptionType.UNAUTHORIZED);
		}finally{
			pm.close();
		}
	}

	public static Group toClientGroup(GroupJDO group){
		Group g = new Group(group.getId());
		g.setName(group.getName());
		g.setDescription(group.getDescription());
		g.setPassword(group.getPassword());
		User user;
		for(PersonJDO person : group.getMembers()){
			Person p = new Person(person.getKey().getId());
			p.setName(person.getName());
			p.setType(person.getType());
			p.setWeight(person.getWeight());
			p.setEmailFreq(person.getEmailFreq());
			user = person.getUser();
			if(user != null){
				p.setUser(new ClientUser(user.getUserId(),
						user.getEmail(),
						user.getNickname()));
			}
			g.addPerson(p);
		}
		for(PaysJDO pays : group.getPayments()){
			Pays p = new Pays(pays.getKey().getId());
			p.setAmount(pays.getAmount());
			p.setDescription(pays.getDescription());
			p.setPayeeID(pays.getPayee().getId());
			p.setPayerID(pays.getPayer().getId());
			p.setDate(pays.getDate());
			g.addPays(p);
		}
		for(BillJDO bill : group.getBills()){
			Bill b = new Bill(bill.getKey().getId());
			b.setAmount(bill.getAmount());
			b.setBuyerID(bill.getBuyer().getId());
			b.setDate(bill.getDate());
			b.setDescription(bill.getDescription());
			b.setPayee(bill.getPayee());
			for(GetsJDO gets : bill.getGets()){
				Gets gt = new Gets(gets.getKey().getId());
				gt.setAmount(gets.getAmount());
				gt.setDescription(gets.getDescription());
				gt.setPersonID(gets.getPerson().getId());
				b.addGets(gt);
			}
			g.addBill(b);
		}
		return g;
	}

	private static final PersistenceManagerFactory PMF =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
