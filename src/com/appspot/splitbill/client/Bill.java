package com.appspot.splitbill.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.appspot.splitbill.client.Group.SuggestionType;
import com.appspot.splitbill.client.util.Formatting;
import com.appspot.splitbill.client.widgets.table.Column;
import com.appspot.splitbill.client.widgets.table.Entry;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class Bill implements Serializable, Entry<Bill, Bill.BillColumn>{

	private static final long serialVersionUID = 8918201773598834847L;
	
	private Group parent;
	private long id, buyerID;
	private Date date;
	private String payee, description;
	private double amount;
	private List<Gets> gets = new ArrayList<Gets>();

	public Bill(){}
	
	public Bill(long id){
		this.id = id;
	}
	
	public Bill(Bill other){
		if(other == null){
			this.date = new Date();
		}else{
			this.parent = other.parent;
			this.id = other.id;
			this.buyerID = other.buyerID;
			this.date = new Date(other.date.getTime());
			this.payee = other.payee;
			this.description = other.description;
			this.amount = other.amount;
			for(Gets get : other.gets){
				this.gets.add(new Gets(get));
			}
		}
	}

	public static Bill newInstance() {
		Bill b = new Bill();
		b.date = new Date();
		return b;
	}
	
	public void setParent(Group parent){
		this.parent = parent;
		for(Gets get : gets){
			get.setParent(parent);
		}
		if(parent != null){
			parent.addSuggestion(SuggestionType.BILL_PAYEE, payee);
			parent.addSuggestion(SuggestionType.BILL_DESC, description);
		}
	}

	public long getID() {
		return id;
	}

	public void setBuyerID(long buyerID) {
		this.buyerID = buyerID;
	}

	public long getBuyerID() {
		return buyerID;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
	
	public String getNiceDate(){
		return Formatting.formatDate(date);
	}

	public void setPayee(String payee) {
		this.payee = payee;
		if(parent != null){
			parent.addSuggestion(SuggestionType.BILL_PAYEE, payee);
		}
	}

	public String getPayee() {
		return payee;
	}

	public void setDescription(String description) {
		this.description = description;
		if(parent != null){
			parent.addSuggestion(SuggestionType.BILL_DESC, description);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setAmount(double amount) {
		this.amount = Math.floor(100f*amount)/100f;
	}

	public double getAmount() {
		return amount;
	}
	
	public String getNiceAmount(){
		return Formatting.formatAmount(amount);
	}
	
	public List<Gets> getGets(){
		return new ArrayList<Gets>(gets);
	}

	public void addGets(Gets g) {
		gets.add(g);
		g.setParent(parent);
	}

	public void removeGets(long getsID) {
		Gets toRemove = null;
		for(Gets gets : this.gets){
			if(gets.getId() == getsID){
				toRemove = gets;
			}
		}
		if(toRemove != null){
			this.gets.remove(toRemove);
			toRemove.setParent(null);
		}
	}
	
	public static enum BillColumn implements Column {
		DATE("Date"),
		PAID_TO("Paid To"),
		MEMO("Memo"),
		AMOUNT("Amount"),
		BUYER("Buyer");
		
		private String title;
		private BillColumn(String title){
			this.title = title;
		}

		@Override
		public String getColumnTitle() {
			return title;
		}
		
	}

	@Override
	public BillColumn[] getColumns() {
		return BillColumn.values();
	}

	@Override
	public Comparator<Bill> getComparator(final BillColumn c) {
		return new Comparator<Bill>(){
			@Override
			public int compare(Bill o1, Bill o2) {
				try{
					switch(c){
					case AMOUNT:
						return Double.compare(o1.amount, o2.amount);
					case BUYER:
						String n1 = o1.parent.getPersonName(o1.buyerID);
						String n2 = o2.parent.getPersonName(o2.buyerID);
						return n1.compareTo(n2);
					case DATE:
						return o1.date.compareTo(o2.date);
					case MEMO:
						return o1.description.compareTo(o2.description);
					case PAID_TO:
						return o1.payee.compareTo(o2.payee);
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
	public Bill getCopy() {
		return new Bill(this);
	}

	@Override
	public Widget getWidget(BillColumn c) {
		switch(c){
		case AMOUNT:
			return new Label(getNiceAmount());
		case BUYER:
			return new Label(parent.getPersonName(buyerID));
		case DATE:
			return new Label(getNiceDate());
		case MEMO:
			return new Label(description);
		case PAID_TO:
			return new Label(payee);
		default:
			assert false : c;
			return null;
		}
	}
	
}
