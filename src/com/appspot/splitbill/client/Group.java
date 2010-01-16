package com.appspot.splitbill.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Group implements Serializable {

	private static final long serialVersionUID = 8959893793197122305L;
	
	private long id;
	private String name, description, password;
	private List<Person> people = new ArrayList<Person>();
	private List<Bill> bills = new ArrayList<Bill>();
	private List<Pays> pays = new ArrayList<Pays>();
	
	private Map<SuggestionType, Set<String>> suggestions = new HashMap<SuggestionType, Set<String>>();
	
	public static enum SuggestionType {
		BILL_DESC, BILL_PAYEE, GETS_DESC, PAYS_DESC;
	}
	
	public Group(){}
	
	public Group(long id){
		this();
		this.id = id;
	}
	
	public void addSuggestion(SuggestionType type, String suggestion){
		if(suggestion != null){
			Set<String> suggestionSet = suggestions.get(type);
			if(suggestionSet == null){
				suggestionSet = new HashSet<String>();
				suggestions.put(type, suggestionSet);
			}
			suggestionSet.add(suggestion);
		}
	}
	
	public Set<String> getSuggestions(SuggestionType type){
		Set<String> suggestionSet = suggestions.get(type);
		if(suggestionSet == null){
			suggestionSet = new HashSet<String>();
		}
		return suggestionSet;
	}
	
	public GroupThumbnail getThumbnail(){
		return new GroupThumbnail(id,name,description,password);
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public List<Person> getPeople(){
		return new ArrayList<Person>(people);
	}
	
	public Person getPerson(long id){
		for(Person person : people){
			if(person.getID() == id){
				return person;
			}
		}
		return null;
	}
	
	public List<Bill> getBills(){
		return new ArrayList<Bill>(bills);
	}
	
	public Bill getBill(long id){
		for(Bill bill : bills){
			if(bill.getID() == id){
				return bill;
			}
		}
		return null;
	}
	
	public List<Pays> getPays(){
		return new ArrayList<Pays>(pays);
	}
	
	public Pays getPays(long id){
		for(Pays pay : pays){
			if(pay.getID() == id){
				return pay;
			}
		}
		return null;
	}

	public void addPerson(Person p) {
		people.add(p);
		updatePercentages();
	}

	public void addPays(Pays p) {
		p.setParent(this);
		pays.add(p);
	}

	public void addBill(Bill b) {
		bills.add(b);
		b.setParent(this);
	}
	
	public Person getPerson(ClientUser user){
		if(user != null){
			for(Person person : people){
				ClientUser pUser = person.getUser();
				if(pUser != null && pUser.equals(user)){
					return person;
				}
			}
		}
		return null;
	}
	
	public boolean isAdmin(ClientUser user){
		Person p = getPerson(user);
		return p != null && p.getType() == PersonType.ADMIN;
	}
	
	public double personPercent(long personID){
		double n = 0f, sum = 0f, pWeight = 0f;
		for(Person person : people){
			if(person.getType() != PersonType.WATCHER){
				n++;
				sum += person.getWeight();
				if(person.getID() == personID){
					pWeight = person.getWeight();
				}
			}
		}
		if(sum != 0){
			return pWeight/sum;
		}else{
			return 1f/n;
		}
	}
	
	public void updatePercentages(){
		double n = 0f, sum = 0f;
		for(Person person : people){
			if(person.getType() != PersonType.WATCHER){
				n++;
				sum += person.getWeight();
			}
		}
		for(Person person : people){
			if(person.getType() != PersonType.WATCHER){
				if(sum != 0){
					person.setPercent(100f * person.getWeight() / sum);
				}else{
					person.setPercent(100f * 1f / n);
				}
			}
		}
	}

	public void removePerson(long personID) {
		Person toRemove = null;
		for(Person person : people){
			if(person.getID() == personID){
				toRemove = person;
				break;
			}
		}
		if(toRemove != null){
			people.remove(toRemove);
			updatePercentages();
		}
	}
	
	public void removeBill(long billID) {
		Bill toRemove = null;
		for(Bill bill : bills){
			if(bill.getID() == billID){
				toRemove = bill;
				break;
			}
		}
		if(toRemove != null){
			bills.remove(toRemove);
			toRemove.setParent(null);
		}
	}
	
	public void removePays(long paysID){
		Pays toRemove = null;
		for(Pays pay : pays){
			if(pay.getID() == paysID){
				toRemove = pay;
				break;
			}
		}
		if(toRemove != null){
			pays.remove(toRemove);
			toRemove.setParent(null);
		}
	}
	
	public String getPersonName(long personID){
		for(Person person : people){
			if(person.getID() == personID){
				return person.getName();
			}
		}
		return "None";
	}
	
	public boolean personExists(long personID){
		for(Person person : people){
			if(person.getID() == personID){
				return true;
			}
		}
		return false;
	}
	
	public double pot(){
		double pot = 0f;
		for(Bill bill : bills){
			if(personExists(bill.getBuyerID())){
				pot += bill.getAmount();
				for(Gets gets : bill.getGets()){
					if(personExists(gets.getPersonID())){
						pot -= gets.getAmount();
					}
				}
			}
		}
		return pot;
	}
	
	public double pot(Date asOf){
		double pot = 0f;
		if(asOf == null){
			asOf = new Date(Long.MAX_VALUE);
		}
		for(Bill bill : bills){
			if(personExists(bill.getBuyerID()) &&
					bill.getDate().before(asOf)){
				pot += bill.getAmount();
				for(Gets gets : bill.getGets()){
					if(personExists(gets.getPersonID())){
						pot -= gets.getAmount();
					}
				}
			}
		}
		return pot;
	}
	
	public Owes getOwes(long personID){
		for(Owes owe : getOwes()){
			if(owe.getPersonID() == personID){
				return owe;
			}
		}
		return null;
	}
	
	public List<Owes> getOwes(){
		double pot = pot();
		List<Owes> owes = new ArrayList<Owes>();
		for(Person person : people){
			long id = person.getID();
			double amount = 0f;
			amount += personPercent(id)*pot;
			for(Bill bill : bills){
				if(bill.getBuyerID() == id){
					amount -= bill.getAmount();
				}
				for(Gets gets : bill.getGets()){
					if(gets.getPersonID() == id){
						amount += gets.getAmount();
					}
				}
			}
			for(Pays pays : this.pays){
				if(pays.getPayeeID() == id){
					amount += pays.getAmount();
				}
				if(pays.getPayerID() == id){
					amount -= pays.getAmount();
				}
			}
			owes.add(new Owes(id,person.getName(),amount));
		}
		return owes;
	}
	
	public double getSpent(long personID){
		double pc = personPercent(personID);
		double pot = pot();
		double fromPot = pc*pot;
		
		double fromGets = 0d;
		for(Bill bill : bills){
			for(Gets get : bill.getGets()){
				if(get.getPersonID() == personID){
					fromGets += get.getAmount();
				}
			}
		}
		
		return fromPot + fromGets;
	}
	
	public double getPaid(long personID){
		double sum = 0d;
		
		for(Bill bill : bills){
			if(bill.getBuyerID() == personID){
				sum += bill.getAmount();
			}
		}
		
		for(Pays pay : pays){
			if(pay.getPayerID() == personID){
				sum += pay.getAmount();
			}
			if(pay.getPayeeID() == personID){
				sum -= pay.getAmount();
			}
		}
		
		return sum;
	}
	
}
