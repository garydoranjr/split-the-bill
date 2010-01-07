package com.appspot.splitbill.client.widgets.editor;

import com.google.gwt.uibinder.client.UiConstructor;

public class MinimumLengthTextBox extends TypedTextBox<String> {

	private int minLength;
	
	public MinimumLengthTextBox(int minLength){
		this.minLength = minLength;
	}
	
	public @UiConstructor MinimumLengthTextBox(String minLength){
		this.minLength = Integer.parseInt(minLength);
	}
	
	@Override
	protected String getValue(String textValue) {
		String retVal = textValue.trim();
		if(retVal.length() < minLength){
			return null;
		}else{
			return retVal;
		}
	}

}
