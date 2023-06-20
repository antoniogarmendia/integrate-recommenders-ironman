package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.wizard.pages.contents.SelectItemContentProvider;
import integrate.recommenders.ironman.wizard.pages.label.SelectItemRecommenderProvider;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectTargetItems extends WizardPage {
	
	private CheckboxTreeViewer checkboxTreeViewer;
	
	private boolean refresh;
	
	protected SelectTargetItems(String pageName) {
		super(pageName);	
		setTitle(IRONMAN_WIZARD_PAGE_SELECT_TARGET_ITEMS_NAME);
		this.refresh = true;
	}

	@Override
	public void createControl(Composite parent) {
		//Container Composite
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().create());
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL,true,true,1,1));
		
		checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.VIRTUAL | SWT.BORDER | SWT.CHECK ); 
		checkboxTreeViewer.getTree().setHeaderVisible(true);
		checkboxTreeViewer.getTree().setLinesVisible(true);
		checkboxTreeViewer.setUseHashlookup(true);
		
		checkboxTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createColumns(checkboxTreeViewer);
				
		checkboxTreeViewer.setContentProvider(new SelectItemContentProvider());	
				
		checkboxTreeViewer.getTree().addSelectionListener(selectTreeViewerItem());
		
		final Composite configureTree = new Composite(container, SWT.NONE);
		configureTree.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		
		WidgetFactory.button(SWT.NONE).text("Expand All").onSelect(
				e -> {this.checkboxTreeViewer.expandAll();}).create(configureTree);
		
		WidgetFactory.button(SWT.NONE).text("Collapse All").onSelect(
				e -> {this.checkboxTreeViewer.collapseAll();})
						.create(configureTree);		
		
		setControl(container);
		setPageComplete(true);		
	}
	
	private void createColumns(CheckboxTreeViewer checkboxTreeViewer2) {
		//Choose Items Column
		TreeViewerColumn itemColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		itemColumn.getColumn().setWidth(280);
		itemColumn.getColumn().setText("Select Items");
			
		//Provider Diagram Description Column
		itemColumn.setLabelProvider(new SelectItemRecommenderProvider());		
	}

	@Override
	public IronManWizard getWizard() {
		return (IronManWizard) super.getWizard();
	}
	
	public Map<String,List<Service>> mapServerToSelectedRecommender() {
		return ((SelectRecommenders)getWizard()
				.getPage(IronManWizard.SELECT_RECOMMENDER_PAGE_NAME)).mapServerToSelectedRecommender();
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true & this.refresh == true) {
			this.checkboxTreeViewer.setInput(mapServerToSelectedRecommender()
					.values().stream().flatMap(List::stream).collect(Collectors.toList()));
			this.checkboxTreeViewer.refresh();	
			this.checkboxTreeViewer.expandAll();
			this.refresh = false;
		}
	}
	
	public CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}
	
	public Map<String,List<Service>> getSelectedServerToRecommender() {
		final Map<String,List<Service>> selectedServerToRecommender = 
				new HashMap<String, List<Service>>();
		final Object[] selectedElements = this.checkboxTreeViewer.getCheckedElements();
		List<Service> currentRecList = null;
		List<String> currrentItems = null;
		for (Object object : selectedElements) {
			if (object instanceof Map.Entry) {
				final Map.Entry<?,?> entryMap = (Map.Entry<?,?>) object;
				final String url =  (String) entryMap.getKey();
				currentRecList = new ArrayList<Service>();
				selectedServerToRecommender.put(url, currentRecList);				
			} else if (object instanceof Service) {
				final Service rec = (Service) object;
				//TODO fix
//				final Service copyRec = new Service(rec);
//				copyRec.getDetail().getItems().clear();
//				currrentItems = new ArrayList<String>();
//				copyRec.getDetail().setItems(currrentItems);
//				currentRecList.add(rec);				
			} else if (object instanceof String) {
				currrentItems.add((String) object);				
			}			
		}		
		return selectedServerToRecommender;
	}
}
