package com.appspot.splitbill.client;

import java.io.Serializable;
import java.util.Comparator;

import com.appspot.splitbill.client.widgets.table.Column;
import com.appspot.splitbill.client.widgets.table.Entry;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Person implements Serializable, Entry<Person, Person.PersonColumn>{
	
	private static final long serialVersionUID = -9098481678847198158L;
	
	private static final double W_DECIMALS = 3.0, P_DECIMALS = 1.0;
	
	private long id;
	private String name;
	private ClientUser user;
	private double weight;
	private PersonType type;
	private EmailFrequency emailFreq;
	private double percent = 0;
	
	public Person(){}
	
	public Person(long id){
		this.id = id;
	}
	
	public Person(Person other){
		if(other != null){
			this.id = other.id;
			this.name = other.name;
			this.user = other.user == null ? null : new ClientUser(other.user);
			this.weight = other.weight;
			this.type = other.type;
			this.emailFreq = other.emailFreq;
		}else{
			this.weight = 1.0;
			this.type = PersonType.REGULAR;
			this.emailFreq = EmailFrequency.NEVER;
		}
	}
	
	public static Person newInstance(){
		Person p = new Person();
		p.name = "New Person";
		p.type = PersonType.REGULAR;
		p.emailFreq = EmailFrequency.NEVER;
		p.weight = 1.0;
		return p;
	}

	public long getID() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUser(ClientUser user) {
		this.user = user;
	}

	public ClientUser getUser() {
		return user;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setPercent(double pc){
		this.percent = pc;
	}
	
	public double getPercent(){
		return percent;
	}

	public void setType(PersonType type) {
		this.type = type;
	}

	public PersonType getType() {
		return type;
	}
	
	public void setEmailFreq(EmailFrequency freq){
		this.emailFreq = freq;
	}
	
	public EmailFrequency getEmailFreq(){
		return emailFreq == null ? EmailFrequency.NEVER : emailFreq;
	}
	
	public static enum PersonColumn implements Column{
		NAME("Name"),
		USER("Gmail"),
		WEIGHT("Weight"),
		PERCENT("Percent"),
		ADMIN("Administrator?");
		
		private String columnTitle;
		
		private PersonColumn(String columnTitle){
			this.columnTitle = columnTitle;
		}

		@Override
		public String getColumnTitle() {
			return columnTitle;
		}
	}

	@Override
	public PersonColumn[] getColumns() {
		return PersonColumn.values();
	}

	@Override
	public Comparator<Person> getComparator(final PersonColumn c) {
		return new Comparator<Person>(){

			@Override
			public int compare(Person o1, Person o2) {
				try{
					switch(c){
					case NAME:
						return o1.name.compareTo(o2.name);
					case ADMIN:
						return o1.type.compareTo(o2.type);
					case PERCENT:
					case WEIGHT:
						return Double.compare(o1.weight, o2.weight);
					case USER:
						String n1 = "", n2 = "";
						if(o1.user != null){
							n1 = o1.user.getNickname();
						}
						if(o2.user != null){
							n2 = o2.user.getNickname();
						}
						return n1.compareTo(n2);
					default:
						assert false : c;
						return 0;
					}
				}catch(NullPointerException e){
					return 0;
				}
			}
			
		};
	}

	@Override
	public Person getCopy() {
		return new Person(this);
	}

	@Override
	public Widget getWidget(PersonColumn c) {
		double rounded;
		switch(c){
		case NAME:
			return new Label(name);
		case USER:
			if(user == null){
				return new HTML();
			}else{
				return new HTML("<a href=\"mailto:"+user.getEmail()+"\">"
						+user.getNickname()+"</a>");
			}
		case WEIGHT:
			rounded = (Math.round(weight*Math.pow(10.,W_DECIMALS)) /
					Math.pow(10., W_DECIMALS));
			return new Label(""+rounded);
		case PERCENT:
			rounded = (Math.round(percent*Math.pow(10.,P_DECIMALS)) /
					Math.pow(10., P_DECIMALS));
			return new Label(rounded + "%");
		case ADMIN:
			return new Label( type == PersonType.ADMIN ? "Yes" : "No");
		default:
			assert false : c;
			return null;
		}
	}
	
}
