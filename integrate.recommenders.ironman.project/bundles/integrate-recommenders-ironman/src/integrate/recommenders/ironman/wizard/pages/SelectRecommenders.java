package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.wizard.pages.contents.LanguageRecommenderContentProvider;
import integrate.recommenders.ironman.wizard.pages.label.ItemRecommenderProvider;
import integrate.recommenders.ironman.wizard.pages.label.LanguageRecommenderProvider;
import integrate.recommenders.ironman.wizard.pages.label.TargetRecommenderProvider;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SelectRecommenders extends WizardPage {
	
	private CheckboxTreeViewer checkboxTreeViewer;
	
	protected SelectRecommenders(String pageName) {
		super(pageName);	
		setTitle(IRONMAN_WIZARD_PAGE_SELECT_RECOMMENDER_NAME);		
	}

	@Override
	public void createControl(Composite parent) {
		//Scrolled Composite
		//final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		final Composite sc = new Composite(parent, SWT.NONE);
		sc.setLayout(GridLayoutFactory.fillDefaults().create());
		sc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL,GridData.FILL_VERTICAL,true,true,1,1));
		
		checkboxTreeViewer = new CheckboxTreeViewer(sc, SWT.VIRTUAL | SWT.BORDER | SWT.CHECK ); 
		checkboxTreeViewer.getTree().setHeaderVisible(true);
		checkboxTreeViewer.getTree().setLinesVisible(true);
		checkboxTreeViewer.setUseHashlookup(true);
		
		//checkboxTreeViewer.getTree().setLayoutData(GridDataFactory.fillDefaults().create());
		checkboxTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createColumns(checkboxTreeViewer);	
		
		//sc.setExpandHorizontal(true);
		//sc.setExpandVertical(true);
		//sc.setAlwaysShowScrollBars(true);
		
		//sc.setContent(checkboxTreeViewer.getTree());
		
		checkboxTreeViewer.setContentProvider(new LanguageRecommenderContentProvider());	
		checkboxTreeViewer.setInput(getWizard().getAllRecommenders());		
		
		final Composite configureTree = new Composite(sc, SWT.NONE);
		configureTree.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		WidgetFactory.button(SWT.NONE).text("Expand All").onSelect(
				e -> {this.checkboxTreeViewer.expandAll();}).create(configureTree);
		
		WidgetFactory.button(SWT.NONE).text("Collapse All").onSelect(
				e -> {this.checkboxTreeViewer.collapseAll();})
						.create(configureTree);
		
		setControl(sc);
		setPageComplete(true);		
	}
	
	private void createColumns(CheckboxTreeViewer checkboxTreeViewer) {
		//Language & Recommenders Column
		TreeViewerColumn languageColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		languageColumn.getColumn().setWidth(180);
		languageColumn.getColumn().setText("Language And Recommenders");
		
		//Provider Diagram Description Column
		languageColumn.setLabelProvider(new LanguageRecommenderProvider());
		
		//Target Column
		TreeViewerColumn targetColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		targetColumn.getColumn().setWidth(180);
		targetColumn.getColumn().setText("Target");
		
		//Provider Diagram Description Column
		targetColumn.setLabelProvider(new TargetRecommenderProvider());
		
		//Items Column
		TreeViewerColumn itemsColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		itemsColumn.getColumn().setWidth(180);
		itemsColumn.getColumn().setText("Items");
		
		//Provider Diagram Description Column
		itemsColumn.setLabelProvider(new ItemRecommenderProvider());		
	}

	@Override
	public IronManWizard getWizard() {
		return (IronManWizard) super.getWizard();
	}	
	
	public Map<String,List<Recommender>> mapServerToSelectedRecommender() {
		final Object[] selectedObjects = checkboxTreeViewer.getCheckedElements();
		final Map<String,List<Recommender>> mapServerToSelectedRecommender = new HashMap<String,List<Recommender>>();
		String server = null;
		for (Object object : selectedObjects) {
			if (object instanceof Map.Entry) {
				final Entry<?, ?> serversToServices = (Map.Entry<?, ?>) object;
				server = (String) serversToServices.getKey();	
				mapServerToSelectedRecommender.put(server, new ArrayList<Recommender>());
			} else if (object instanceof Recommender) {
				mapServerToSelectedRecommender.get(server).add((Recommender)object);
			}
		}		
		return mapServerToSelectedRecommender;
	}
	
	public CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}
}
