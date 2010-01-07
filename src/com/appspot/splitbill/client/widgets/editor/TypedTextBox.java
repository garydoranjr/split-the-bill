package com.appspot.splitbill.client.widgets.editor;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class TypedTextBox<T> extends Composite implements Editor{

	private static TypedTextFieldUiBinder uiBinder = GWT
			.create(TypedTextFieldUiBinder.class);
	@SuppressWarnings("unchecked")
	interface TypedTextFieldUiBinder extends UiBinder<Widget, TypedTextBox> {}
	
	interface MyStyle extends CssResource {
		String invalid();
	}

	@UiField MyStyle style;
	@UiField TextBox textBox;

	public TypedTextBox() {
		initWidget(uiBinder.createAndBindUi(this));
		changed();
	}

	@UiHandler("textBox") void onKeyUp(KeyUpEvent event) {
		changed();
	}

	@Override
	public boolean isEditOK() {
		return (getValue() != null);
	}
	
	public void setValue(T value){
		textBox.setValue(value.toString());
		changed();
	}
	
	public T getValue(){
		return getValue(textBox.getValue());
	}
	
	protected abstract T getValue(String textValue);

	private List<EditorChangeListener> listeners = new LinkedList<EditorChangeListener>();
	
	public final void changed(){
		if(isEditOK()){
			textBox.removeStyleName(style.invalid());
		}else{
			textBox.addStyleName(style.invalid());
		}
		for(EditorChangeListener listener : listeners){
			listener.editorChanged(this);
		}
	}
	
	@Override
	public final void addChangeListener(EditorChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public final void removeChangeListener(EditorChangeListener listener) {
		listeners.remove(listener);
	}

}
