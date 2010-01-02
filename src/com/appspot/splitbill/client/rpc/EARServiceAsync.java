package com.appspot.splitbill.client.rpc;

import com.appspot.splitbill.client.Bill;
import com.appspot.splitbill.client.ClientUser;
import com.appspot.splitbill.client.Gets;
import com.appspot.splitbill.client.Pays;
import com.appspot.splitbill.client.Person;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EARServiceAsync {

	public void removePerson(long groupID, long personID, AsyncCallback<Void> asyncCallback);
	public void editPerson(long groupID, Person person, AsyncCallback<ClientUser> asyncCallback);
	public void addPerson(long groupID, Person person, AsyncCallback<Person> asyncCallback);
	
	public void addBill(long groupID, Bill bill, AsyncCallback<Bill> asyncCallback);
	public void removeBill(long groupID, long billID, AsyncCallback<Void> asyncCallback);
	public void editBill(long groupID, Bill bill, AsyncCallback<Void> asyncCallback);
	
	public void addGets(long groupID, long billID, Gets gets, AsyncCallback<Gets> asyncCallback);
	public void removeGets(long groupID, long billID, long getsID, AsyncCallback<Void> asyncCallback);
	
	public void addPays(long groupID, Pays pays, AsyncCallback<Pays> asyncCallback);
	public void removePays(long groupID, long paysID, AsyncCallback<Void> asyncCallback);
	public void editPays(long groupID, Pays pays, AsyncCallback<Void> asyncCallback);
}
