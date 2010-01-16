package com.appspot.splitbill.client;

import java.io.Serializable;
import java.util.Comparator;

import com.appspot.splitbill.client.Group.SuggestionType;
import com.appspot.splitbill.client.util.Formatting;
import com.appspot.splitbill.client.widgets.table.Column;
import com.appspot.splitbill.client.widgets.table.Entry;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Gets implements Serializable, Entry<Gets, Gets.GetsColumn> {
	
	private static final long serialVersionUID = -3568295062759209930L;
	
	private Group parent;
	private long id, personID;
	private double amount;
	private String description;
	
	public Gets(){}
	
	public Gets(long id){
		this.id = id;
	}
	
	public Gets(Gets other){
		if(other != null){
			this.parent = other.parent;
			this.id = other.id;
			this.personID = other.personID;
			this.amount = other.amount;
			this.description = other.description;
		}
	}

	public static Gets newInstance() {
		return new Gets();
	}
	
	public void setParentWithoutSuggestion(Group parent){
		this.parent = parent;
	}

	public void setParent(Group parent){
		this.parent = parent;
		if(parent != null){
			parent.addSuggestion(SuggestionType.GETS_DESC, description);
		}
	}
	
	public long getId() {
		return id;
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public long getPersonID() {
		return personID;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
	
	public String getNiceAmount(){
		return Formatting.formatAmount(amount);
	}

	public void setDescription(String description) {
		this.description = description;
		if(parent != null){
			parent.addSuggestion(SuggestionType.GETS_DESC, description);
		}
	}

	public String getDescription() {
		return description;
	}
	
	public static enum GetsColumn implements Column {
		MEMBER("Member"),
		AMOUNT("Amount"),
		MEMO("Memo");

		private String title;
		private GetsColumn(String title){
			this.title = title;
		}
		
		@Override
		public String getColumnTitle() {
			return title;
		}
		
	}

	@Override
	public GetsColumn[] getColumns() {
		return GetsColumn.values();
	}

	@Override
	public Comparator<Gets> getComparator(final GetsColumn c) {
		return new Comparator<Gets>(){
			@Override
			public int compare(Gets o1, Gets o2) {
				try{
					switch(c){
					case AMOUNT:
						return Double.compare(o1.amount, o2.amount);
					case MEMBER:
						String n1 = o1.parent.getPersonName(o1.personID);
						String n2 = o2.parent.getPersonName(o2.personID);
						return n1.compareTo(n2);
					case MEMO:
						return o1.description.compareTo(o2.description);
					default:
						assert false : c;
						return 0;
					}
				}catch(NullPointerException e){
					e.printStackTrace();
					return 0;
				}
			}
		};
	}

	@Override
	public Gets getCopy() {
		return new Gets(this);
	}

	@Override
	public Widget getWidget(GetsColumn c) {
		switch(c){
		case AMOUNT:
			return new Label(getNiceAmount());
		case MEMBER:
			return new Label(parent.getPersonName(personID));
		case MEMO:
			return new Label(description);
		default:
			assert false : c;
			return null;
		}
	}
}
