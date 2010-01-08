package com.appspot.splitbill.client;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.appspot.splitbill.client.Group.SuggestionType;
import com.appspot.splitbill.client.util.Formatting;
import com.appspot.splitbill.client.widgets.table.Column;
import com.appspot.splitbill.client.widgets.table.Entry;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Pays implements Serializable, Entry<Pays, Pays.PaysColumn> {
	
	private static final long serialVersionUID = 854218973563349571L;
	private Group parent = null;
	private long id, payeeID, payerID;
	private double amount;
	private String description;
	private Date date;
	
	public Pays(){}
	
	public Pays(long id){
		this.id = id;
	}
	
	public Pays(Pays other){
		if(other != null){
			this.parent = other.parent;
			this.id = other.id;
			this.payeeID = other.payeeID;
			this.payerID = other.payerID;
			this.amount = other.amount;
			this.description = other.description;
			this.date = other.date;
		}
	}
	
	public static Pays newInstance(){
		Pays p = new Pays();
		p.date = new Date();
		return p;
	}
	
	public void setParent(Group parent){
		this.parent = parent;
		if(parent != null){
			parent.addSuggestion(SuggestionType.PAYS_DESC, description);
		}
	}

	public long getID() {
		return id;
	}

	public void setPayeeID(long payeeID) {
		this.payeeID = payeeID;
	}

	public long getPayeeID() {
		return payeeID;
	}

	public void setPayerID(long payerID) {
		this.payerID = payerID;
	}

	public long getPayerID() {
		return payerID;
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
			parent.addSuggestion(SuggestionType.PAYS_DESC, description);
		}
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public String getNiceDate(){
		return Formatting.formatDate(date);
	}
	
	public static enum PaysColumn implements Column{
		DATE("Date"),
		PAYER("Payer"),
		PAYEE("Payee"),
		AMOUNT("Amount"),
		DESC("Description");

		private String title;
		private PaysColumn(String title){
			this.title = title;
		}
		
		@Override
		public String getColumnTitle() {
			return title;
		}
		
	}

	@Override
	public PaysColumn[] getColumns() {
		return PaysColumn.values();
	}

	@Override
	public Comparator<Pays> getComparator(final PaysColumn c) {
		return new Comparator<Pays>(){
			@Override
			public int compare(Pays o1, Pays o2) {
				String n1, n2;
				try{
					switch(c){
					case AMOUNT:
						return Double.compare(o1.amount, o2.amount);
					case DATE:
						return o1.date.compareTo(o2.date);
					case DESC:
						return o1.description.compareTo(o2.description);
					case PAYEE:
						n1 = o1.parent.getPersonName(o1.payeeID);
						n2 = o2.parent.getPersonName(o2.payeeID);
						return n1.compareTo(n2);
					case PAYER:
						n1 = o1.parent.getPersonName(o1.payerID);
						n2 = o2.parent.getPersonName(o2.payerID);
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
	public Pays getCopy() {
		return new Pays(this);
	}

	@Override
	public Widget getWidget(PaysColumn c) {
		switch(c){
		case AMOUNT:
			return new Label(getNiceAmount());
		case DATE:
			return new Label(getNiceDate());
		case DESC:
			return new Label(getDescription());
		case PAYEE:
			return new Label(parent.getPersonName(payeeID));
		case PAYER:
			return new Label(parent.getPersonName(payerID));
		default:
			assert false : c;
			return null;
		}
	}

}
