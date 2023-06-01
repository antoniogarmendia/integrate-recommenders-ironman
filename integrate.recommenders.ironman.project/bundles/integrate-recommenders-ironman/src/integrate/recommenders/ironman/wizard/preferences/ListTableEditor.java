package integrate.recommenders.ironman.wizard.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ListTableEditor extends FieldEditor {
	
	private TableViewer tableViewer;
	
	private String oldValue;
	
	public ListTableEditor(final String listOfServers, Composite parent) {
		this.setPreferenceName(listOfServers); 
		createControl(parent);		
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) tableViewer.getTable().getLayoutData();
		gd.horizontalSpan = numColumns;
		// We only grab excess space if we have to
		// If another field editor has more columns then
		// we assume it is setting the width.
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;		
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		tableViewer = new TableViewer(parent);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		//GridData
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		tableViewer.getTable().setLayoutData(gd);
		
		createURIColumn();
		
		final Text textURL = WidgetFactory.text(SWT.NONE).create(parent);
		GridData gdTextURL = new GridData();
		gdTextURL.horizontalSpan = numColumns - 1;
		gdTextURL.horizontalAlignment = GridData.FILL;
		gdTextURL.grabExcessHorizontalSpace = true;
		
		textURL.setLayoutData(gdTextURL);
		
		//Add Button
		WidgetFactory.button(SWT.PUSH).text("Add").onSelect(e -> {
			if (!textURL.getText().isBlank()) {
				String value = getPreferenceStore().getString(getPreferenceName());
				String newValue = value + "|" + textURL.getText();
				getPreferenceStore().setValue(getPreferenceName(), newValue);
				doLoad();
				textURL.setText("");
				tableViewer.getCellEditors()[tableViewer.getCellEditors().length - 1].setFocus();
			}			
		}).create(parent);
				
		
		WidgetFactory.button(SWT.PUSH).text("Remove").onSelect(e -> System.out.println("asd")).create(parent);
		
		tableViewer.setContentProvider(new ServerContentProvider());	   	    
	}

	private void createURIColumn() {
		//Column
		final TableViewerColumn  viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		viewerColumn.getColumn().setText("URLs");
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setMoveable(true);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof String)
					return (String)element;
				return super.getText(element);
			}
		});		
	}

	@Override
	protected void doLoad() {
		if (tableViewer != null) {
			String value = getPreferenceStore().getString(getPreferenceName());
			tableViewer.setInput(value);
			oldValue = value;
		}		
	}

	@Override
	protected void doLoadDefault() {
		if (tableViewer != null) {
			final String value = getPreferenceStore().getDefaultString(
					getPreferenceName());
			tableViewer.setInput(value);
		}
		//TODO valueChanges?
		//valueChanged();		
	}

	@Override
	protected void doStore() {
		final TableItem[] items = tableViewer.getTable().getItems();
		final StringBuilder concatURL = new StringBuilder();
		for (TableItem tableItem : items) {
			concatURL.append((String)tableItem.getData() + '|');
		}
		concatURL.deleteCharAt(concatURL.length() - 1);
		getPreferenceStore().setValue(getPreferenceName(), concatURL.toString());		
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

}
