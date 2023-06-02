package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.wizard.pages.contents.SelectItemContentProvider;
import integrate.recommenders.ironman.wizard.pages.label.SelectItemRecommenderProvider;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		//Scrolled Composite
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setLayoutData(GridDataFactory.fillDefaults().create());		
		
		checkboxTreeViewer = new CheckboxTreeViewer(sc, SWT.VIRTUAL | SWT.BORDER | SWT.CHECK ); 
		checkboxTreeViewer.getTree().setHeaderVisible(true);
		checkboxTreeViewer.getTree().setLinesVisible(true);
		checkboxTreeViewer.setUseHashlookup(true);
		
		checkboxTreeViewer.getTree().setLayoutData(GridDataFactory.fillDefaults().create());
		
		createColumns(checkboxTreeViewer);
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setAlwaysShowScrollBars(true);
		
		sc.setContent(checkboxTreeViewer.getTree());
		
		checkboxTreeViewer.setContentProvider(new SelectItemContentProvider());	
		checkboxTreeViewer.setInput(mapServerToSelectedRecommender());
		
		setControl(sc);
		setPageComplete(true);		
	}
	
	private void createColumns(CheckboxTreeViewer checkboxTreeViewer2) {
		//Choose Items Column
		TreeViewerColumn itemColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		itemColumn.getColumn().setWidth(180);
		itemColumn.getColumn().setText("Select Items");
			
		//Provider Diagram Description Column
		itemColumn.setLabelProvider(new SelectItemRecommenderProvider());		
	}

	@Override
	public IronManWizard getWizard() {
		return (IronManWizard) super.getWizard();
	}
	
	public Map<String,List<Recommender>> mapServerToSelectedRecommender() {
		return ((SelectRecommenders)getWizard()
				.getPage(IronManWizard.SELECT_RECOMMENDER_PAGE_NAME)).mapServerToSelectedRecommender();
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true & this.refresh == true) {
			checkboxTreeViewer.setInput(mapServerToSelectedRecommender());
			checkboxTreeViewer.refresh();	
			this.refresh = false;
		}
	}
	
	public CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}
	
	public Map<String,List<Recommender>> getSelectedServerToRecommender() {
		final Map<String,List<Recommender>> selectedServerToRecommender = 
				new HashMap<String, List<Recommender>>();
		final Object[] selectedElements = this.checkboxTreeViewer.getCheckedElements();
		List<Recommender> currentRecList = null;
		List<String> currrentItems = null;
		for (Object object : selectedElements) {
			if (object instanceof Map.Entry) {
				final Map.Entry<?,?> entryMap = (Map.Entry<?,?>) object;
				final String url =  (String) entryMap.getKey();
				currentRecList = new ArrayList<Recommender>();
				selectedServerToRecommender.put(url, currentRecList);				
			} else if (object instanceof Recommender) {
				final Recommender rec = (Recommender) object;
				final Recommender copyRec = new Recommender(rec);
				copyRec.getDetails().getItems().clear();
				currrentItems = new ArrayList<String>();
				copyRec.getDetails().setItems(currrentItems);
				currentRecList.add(rec);				
			} else if (object instanceof String) {
				currrentItems.add((String) object);				
			}			
		}		
		return selectedServerToRecommender;
	}
}
