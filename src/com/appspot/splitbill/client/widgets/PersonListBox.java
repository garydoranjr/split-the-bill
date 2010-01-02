package com.appspot.splitbill.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.appspot.splitbill.client.Person;
import com.appspot.splitbill.client.PersonType;
import com.google.gwt.user.client.ui.ListBox;

public class PersonListBox extends ListBox {
	
	long[] ids = new long[0];

	public PersonListBox(){
		super(false);
	}
	
	public void setData(List<Person> people, String initialValue){
		this.clear();
		List<Person> members = new ArrayList<Person>();
		for(Person person : people){
			if(person.getType() != PersonType.WATCHER){
				members.add(person);
			}
		}
		ids = new long[members.size()];
		int indx = 0;
		int toSelect = 0;
		for(Person person : members){
			if(person.getName().equals(initialValue)){
				toSelect = indx;
			}
			this.addItem(person.getName());
			ids[indx] = person.getID();
			indx++;
		}
		setSelectedIndex(toSelect);
	}
	
	public long getSelectedID(){
		try{
			return ids[getSelectedIndex()];
		}catch(ArrayIndexOutOfBoundsException e){
			return -1;
		}
	}
	
}
