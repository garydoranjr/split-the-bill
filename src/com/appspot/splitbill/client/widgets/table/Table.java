package com.appspot.splitbill.client.widgets.table;

import java.util.ArrayList;
import java.util.List;

import com.appspot.splitbill.client.widgets.table.TableModel.TableModelChangeListener;
import com.appspot.splitbill.client.widgets.table.control.CloseEntryControl;
import com.appspot.splitbill.client.widgets.table.control.Control;
import com.appspot.splitbill.client.widgets.table.control.DeleteEntryControl;
import com.appspot.splitbill.client.widgets.table.control.DeleteSelectedControl;
import com.appspot.splitbill.client.widgets.table.control.GlueControl;
import com.appspot.splitbill.client.widgets.table.control.NewEntryControl;
import com.appspot.splitbill.client.widgets.table.control.SaveEntryControl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Table extends Composite implements TableModelChangeListener{

	private static TableUiBinder uiBinder = GWT.create(TableUiBinder.class);
	interface TableUiBinder extends UiBinder<Widget, Table> {}
	
	interface MyStyle extends CssResource {
		String mouseOver();
		String selected();
		String tableRow();
		String tableHeader();
		String topLeft();
	}
	
	@UiField MyStyle style;
	@UiField HorizontalPanel controlsTop;
	@UiField HorizontalPanel controlsBottom;
	@UiField VerticalPanel content;
	
	private TableModel model = null;
	private EntryMode entryModeOnClick = EntryMode.VIEW;
	private boolean adminControls = false;
	
	
	public Table() {
		initWidget(uiBinder.createAndBindUi(this));
		controlsTop.setSpacing(5);
		controlsBottom.setSpacing(5);
	}
	
	public void setAdminControls(boolean allow){
		adminControls = allow;
		renderTable();
	}
	
	public void setEntryModeOnClick(EntryMode mode){
		this.entryModeOnClick = mode;
	}
	
	public void setTableModel(TableModel model){
		if(this.model != null){
			this.model.removeTableModelChangeListener(this);
			this.model.setParent(null);
		}
		this.model = model;
		if(this.model != null){
			this.model.setParent(this);
			this.model.addTableModelChangeListener(this);
		}
		tableModelChanged();
	}
	
	private void renderTable(){
		content.clear();
		if(model != null){
			final TableWidget widget = new TableWidget(model, adminControls);

			if(adminControls){
				Control nw = new NewEntryControl(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						renderAdder(model.getAdder());
					}
				});
				renderControls(nw,
						new DeleteSelectedControl(widget, model),
						new GlueControl());
			}else{
				renderControls(new GlueControl());
			}

			content.add(widget);
		}
	}
	
	private ClickHandler getCloseHandler(final EntryViewer viewer){
		return new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(viewer != null){
					viewer.close();
				}
				renderTable();
			}
		};
	}
	
	private void renderViewer(EntryViewer viewer, int row){
		if(adminControls){
			renderControls(new DeleteEntryControl(viewer, model, row),
					new CloseEntryControl(getCloseHandler(viewer)),
					new GlueControl());
		}else{
			renderControls(new CloseEntryControl(getCloseHandler(viewer)),
					new GlueControl());
		}
		renderEntry(viewer);
	}
	
	private void renderEditor(EntryEditor editor, int row){
		renderControls(new SaveEntryControl(editor),
				new DeleteEntryControl(editor, model, row),
				new CloseEntryControl(getCloseHandler(editor)),
				new GlueControl());
		renderEntry(editor);
	}
	
	private void renderAdder(EntryEditor adder){
		renderControls(new SaveEntryControl(adder),
				new CloseEntryControl(getCloseHandler(adder)),
				new GlueControl());
		renderEntry(adder);
	}
	
	private void renderEntry(EntryViewer viewer){
		content.clear();
		if(viewer != null){
			content.add(viewer.getPanelWidget());
		}
	}
	
	private void renderControls(Control... controls){
		controlsTop.clear();
		controlsBottom.clear();
		HorizontalPanel[] panels = new HorizontalPanel[]{controlsTop, controlsBottom};
		for(Control control : controls){
			for(HorizontalPanel panel : panels){
				Widget w = control.generateWidget();
				panel.add(w);
				String width = control.getWidth();
				if(width != null){
					panel.setCellWidth(w, width);
				}
			}
		}
	}
	
	public static enum TableMode {
		TABLE_VIEW, ENTRY_VIEW;
	}
	
	public static enum EntryMode {
		VIEW, EDIT, ADD;
	}

	@Override
	public void tableModelChanged() {
		renderTable();
	}
	
	private void headerClicked(int column){
		int d = model.getSortDirection(column);
		if(d <= 0){
			model.setSort(column, true);
		}else{
			assert d > 0;
			model.setSort(column, false);
		}
	}
	
	private void rowClicked(int row){
		if(entryModeOnClick == null){
			return;
		}
		switch(entryModeOnClick){
		case EDIT:
			renderEditor(model.getEditor(row), row);
			break;
		case VIEW:
			renderViewer(model.getViewer(row), row);
			break;
		case ADD:
			renderAdder(model.getAdder());
			break;
		default:
			assert false : entryModeOnClick;
		}
	}
	
	public class TableWidget extends FlexTable implements ClickHandler {
		
		private int rows, columns;
		private boolean[] selected;
		
		final RowFormatter rFormatter = getRowFormatter();
		
		public TableWidget(TableModel model, boolean allowSelect){
			setWidth("100%");
			setCellSpacing(0);
			sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEOVER);
			if(model == null){
				return;
			}
			rows = model.getRowCount();
			columns = model.getColumnCount();
			selected = new boolean[rows];
			
			// Setup Column Headers
			for(int c = 0; c < columns; c++){
				String title = model.getColumnTitle(c);
				int direction = model.getSortDirection(c);
				if(direction > 0){
					title = "\u25B2 " + title;
				}else if(direction < 0){
					title = "\u25BC " + title;
				}else{
					assert direction == 0;
				}
				this.setWidget(0, c + 1, new Label(title));
				getCellFormatter().addStyleName(0,c+1,style.tableHeader());
			}
			getCellFormatter().addStyleName(0,0,style.topLeft());
			
			for(int r = 0; r < rows; r++){
				for(int c = -1; c < columns; c++){
					Widget w;
					if(c < 0){
						if(adminControls){
							final int fr = r;
							CheckBox cb = new CheckBox();
							cb.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
								@Override
								public void onValueChange(ValueChangeEvent<Boolean> event) {
									selectEvent(fr, event.getValue());
								}
							});
							w = cb;
						}else{
							w = new HTML();
						}
					}else{
						w = model.getWidget(r, c);
					}
					this.setWidget(r+1, c+1, w);
					getCellFormatter().addStyleName(r+1,c+1,style.tableRow());
				}
			}
			
			getColumnFormatter().setWidth(0, adminControls ? "28px" : "1px");
			
			addClickHandler(this);
		}
		
		private void selectEvent(int row, boolean selected){
			if(selected){
				rFormatter.addStyleName(row + 1, style.selected());
			}else{
				rFormatter.removeStyleName(row + 1, style.selected());
			}
			this.selected[row] = selected;
		}
		
		public List<Integer> getSelected(){
			List<Integer> retVal = new ArrayList<Integer>();
			for(int i = 0; i < selected.length; i++){
				if(selected[i]){
					retVal.add(i);
				}
			}
			return retVal;
		}

		@Override
		public void onClick(ClickEvent event) {
			Cell cell = getCellForEvent(event);
			int row = cell.getRowIndex();
			int column = cell.getCellIndex();
			if(row > 0 && column > 0){
				rowClicked(row - 1);
			}else if(row == 0 && column > 0){
				headerClicked(column - 1);
			}
		}
		
		@Override
		public void onBrowserEvent(Event e){
			super.onBrowserEvent(e);
			Element td = getEventTargetCell(e);
            if (td == null) return;
            Element tr = DOM.getParent(td);
			switch(DOM.eventGetType(e)){
			case Event.ONMOUSEOUT:
				setStyleName(tr, style.mouseOver(), false);
				break;
			case Event.ONMOUSEOVER:
				setStyleName(tr, style.mouseOver(), true);
				break;
			}
			// Don't apply to header row
			RowFormatter formatter = getRowFormatter();
			formatter.removeStyleName(0, style.mouseOver());
		}
		
	}

}
