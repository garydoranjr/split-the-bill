package com.appspot.splitbill.client.rpc;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Person;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ear")
public interface EARService extends RemoteService {

	public void removePerson(long groupID, long personID) throws ServiceException;
	public Person addPerson(long groupID, Person person) throws ServiceException;
	public ClientUser editPerson(long groupID, Person person) throws ServiceException;
	
	public Bill addBill(long groupID, Bill bill) throws ServiceException;
	public void removeBill(long groupID, long billID) throws ServiceException;
	public void editBill(long groupID, Bill bill) throws ServiceException;
	
	public Gets addGets(long groupID, long billID, Gets gets) throws ServiceException;
	public void removeGets(long groupID, long billID, long getsID) throws ServiceException;

	public Pays addPays(long groupID, Pays pays) throws ServiceException;
	public void removePays(long groupID, long paysID) throws ServiceException;
	public void editPays(long groupID, Pays pays) throws ServiceException;
}
