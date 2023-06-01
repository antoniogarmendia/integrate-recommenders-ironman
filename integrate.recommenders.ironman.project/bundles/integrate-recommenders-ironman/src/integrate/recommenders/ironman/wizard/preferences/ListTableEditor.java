package integrate.recommenders.ironman.wizard.preferences;

import java.util.function.Consumer;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ListTableEditor extends FieldEditor {
	
	private TableViewer tableViewer;
	
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
		tableViewer = new TableViewer(parent, SWT.SINGLE);
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
		WidgetFactory.button(SWT.PUSH).text("Add").onSelect(addURL(textURL)).create(parent);
				
		WidgetFactory.button(SWT.PUSH).text("Remove").onSelect(e -> { 
			final ISelection selection = tableViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structSelection = (IStructuredSelection) selection;
				final Object selectedObject = structSelection.getFirstElement();
				if (selectedObject instanceof String) {
					final String url = (String) selectedObject;
					final String value = getPreferenceStore().getString(getPreferenceName());
					String newValue = value.replace(url, "").replace("||", "|");
					newValue = newValue.substring(newValue.length() - 1).equals("|") 
									? newValue.substring(0, newValue.length() - 1)
									: newValue;
					getPreferenceStore().setValue(getPreferenceName(), newValue);
					doLoad();
				}
			}			
		}).create(parent);
				
		tableViewer.setContentProvider(new ServerContentProvider());	   	    
	}

	private Consumer<SelectionEvent> addURL(final Text textURL) {
		return e -> {
			if (!textURL.getText().isBlank()) {
				String value = getPreferenceStore().getString(getPreferenceName());
				String newValue = value + "|" + textURL.getText();
				getPreferenceStore().setValue(getPreferenceName(), newValue);
				doLoad();
				textURL.setText("");				
			}			
		};
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
			//oldValue = value;
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
