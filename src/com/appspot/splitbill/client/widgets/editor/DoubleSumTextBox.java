package com.appspot.splitbill.client.widgets.editor;

public class DoubleSumTextBox extends TypedTextBox<Double> {

	@Override
	protected Double getValue(String textValue) {
		double total = 0f;
		String[] strValues = textValue.split("\\+");
		for(String strValue : strValues){
			if(strValue.equals("")||strValue.equals(".")){
				continue;
			}
			try{
				double value = Double.parseDouble(strValue);
				total += value;
			}catch(NumberFormatException e){
				return null;
			}
		}
		return new Double(total);
	}

}
