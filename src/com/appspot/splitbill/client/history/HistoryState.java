package com.appspot.splitbill.client.history;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HistoryState {

	private String key = null;
	private HistoryState parent = null;
	private Map<String, String> values = new HashMap<String, String>();
	private Map<String, HistoryState> states = new HashMap<String, HistoryState>();

	public HistoryState(String key){
		if(key == null){
			throw new NullPointerException("State key cannot be \"null\".");
		}
		this.key = key;
	}

	public String getValue(String key){
		return getValue(key, null);
	}

	public String getValue(String key, String def){
		String retVal = key == null ? null : values.get(key);
		return retVal == null ? def : retVal;
	}

	@Deprecated
	public HistoryState getState(String key){
		return key == null ? null : states.get(key);
	}
	
	public void setValue(String key, String value){
		values.put(key, value);
	}
	
	@Deprecated
	public void addState(HistoryState state){
		states.put(state.getKey(), state);
		state.parent = this;
	}

	@Deprecated
	public HistoryState getParent(){
		return parent;
	}

	@Deprecated
	public String getKey(){
		return key;
	}

	public String toToken(){
		String retVal = "[" + key + "=";

		// Add Key-Value Pairs
		for(Entry<String, String> entry : values.entrySet()){
			retVal += "(" + entry.getKey() + "=" + entry.getValue() + ")";
		}

		// Add Sub-States
		for(HistoryState subState : states.values()){
			retVal += subState.toToken();
		}

		retVal += "]";
		return retVal;
	}

	@Override
	public String toString(){
		return toToken();
	}
	
	@Override
	public boolean equals(Object other){
		if((other != null) && (other instanceof HistoryState)){
			
			// Compare Key
			HistoryState that = (HistoryState) other;
			if(!this.key.equals(that.key)){
				return false;
			}
			
			// Compare Key-Value Pairs
			Collection<String> thisKeys = this.values.keySet();
			Collection<String> thatKeys = that.values.keySet();
			if(!thisKeys.containsAll(thatKeys)){
				return false;
			}
			if(!thatKeys.containsAll(thisKeys)){
				return false;
			}
			for(String key : thisKeys){
				String thisVal = this.values.get(key);
				String thatVal = that.values.get(key);
				if(!thisVal.equals(thatVal)){
					return false;
				}
			}
			
			// Compare Sub-states
			thisKeys = this.states.keySet();
			thatKeys = that.states.keySet();
			if(!thisKeys.containsAll(thatKeys)){
				return false;
			}
			if(!thatKeys.containsAll(thisKeys)){
				return false;
			}
			for(String key : thisKeys){
				HistoryState thisState = this.states.get(key);
				HistoryState thatState = that.states.get(key);
				if(!thisState.equals(thatState)){
					return false;
				}
			}
			
			return true;
		}
		return false;
	}

	public static HistoryState parseToken(String token){
		try{
			StringIterator it = new StringIterator(token);
			char first = it.nextChar();
			if(first != '['){
				return null;
			}
			HistoryState retVal = parseState(it);
			if(it.hasNext()){
				return null;
			}
			return retVal;
		}catch(RuntimeException e){
			return null;
		}
	}

	private static HistoryState parseState(StringIterator it){
		String key = it.nextUntil('=');
		it.nextChar();

		HistoryState retVal = new HistoryState(key);

		while(true){
			char ch = it.nextChar();
			switch(ch){
			case '(':
				String k = it.nextUntil('=');
				it.nextChar();
				String v = it.nextUntil(')');
				it.nextChar();
				retVal.setValue(k, v);
				break;
			case '[':
				HistoryState state = parseState(it);
				retVal.addState(state);
				break;
			case ']':
				return retVal;
			default:
				throw new RuntimeException("Unexpected character: "+ch);
			}
		}
	}

	private static class StringIterator {

		private String str;
		private int pos = 0;

		public StringIterator(String str){
			this.str = str;
		}

		public String nextUntil(char ch){
			if(!hasNext()){
				throw new RuntimeException("Iterator empty.");
			}
			int oldPos = pos;
			int i = str.indexOf(ch, pos);
			pos = ( i == -1 ) ? str.length() : i;
			return str.substring(oldPos, pos);
		}

		public char nextChar(){
			if(!hasNext()){
				throw new RuntimeException("Iterator empty.");
			}
			return str.charAt(pos++);
		}

		public boolean hasNext(){
			return pos < str.length();
		}

	}

}
