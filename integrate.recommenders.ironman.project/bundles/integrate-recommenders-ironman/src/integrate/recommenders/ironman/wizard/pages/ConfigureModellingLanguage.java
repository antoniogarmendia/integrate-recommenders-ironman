package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.MapItemElement;
import integrate.recommenders.ironman.definition.mapping.MapTargetElement;
import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.wizard.pages.contents.MLConfigureLanguageContentProvider;
import integrate.recommenders.ironman.wizard.pages.editing.EditingTargetLangElements;
import integrate.recommenders.ironman.wizard.pages.editing.EditingTargetWriteLangElements;
import integrate.recommenders.ironman.wizard.pages.label.MLSourceLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.MLTargetLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.MLWriteFeatureProvider;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.presentation.EcoreActionBarContributor.ExtendedLoadResourceAction.RegisteredPackageDialog;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class ConfigureModellingLanguage extends WizardPage {

	private Label labelNsUri;
	private final MLMappingConfiguration mapping;
	private TreeViewer configureLangTableViewer;
	private Composite tableComposite;
		
	protected ConfigureModellingLanguage(String pageName) {
		super(pageName);
		setTitle(IRONMAN_WIZARD_PAGE_CONFIGURE_MODELLING_LANGUAGE);	
		//Get Selected Target & Items
		mapping = new MLMappingConfiguration(null,null);
		this.tableComposite = null;
	}	

	@Override
	public void createControl(Composite parent) {
		final Composite containerCheck = new Composite(parent, SWT.NONE);		
		containerCheck.setLayout(GridLayoutFactory.fillDefaults().create());		
		//Check if mapping is necessary
		Button checkedButton = WidgetFactory.button(SWT.CHECK)
			.text("Configure a Mapping to Another Language")
			.create(containerCheck);					
		final Composite container = new Composite(containerCheck, SWT.NONE);		
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		checkedButton.addListener(SWT.Selection, enableListener(container));
		//Create composite to select the mapping to a language
		labelNsUri = WidgetFactory.label(SWT.NONE).text("Mapping to Language: ").create(container);
		//Button to Search Package
		buttonChangeModellingLang(container);
		this.tableComposite = new Composite(container, SWT.NONE);
		this.tableComposite.setLayoutData(GridDataFactory.fillDefaults().create());
		//TreeViewer to define the configuration
		configureLangTableViewer = new TreeViewer(tableComposite, SWT.VIRTUAL | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL  );
		configureLangTableViewer.getTree().setHeaderVisible(true);
		configureLangTableViewer.getTree().setLinesVisible(true);
		configureLangTableViewer.setUseHashlookup(true);
		
		configureLangTableViewer.getTree().setLayoutData(GridDataFactory.fillDefaults().create());
		
		createColumns();
		
		GridLayoutFactory.fillDefaults().generateLayout(tableComposite);
				
		configureLangTableViewer.setContentProvider(new MLConfigureLanguageContentProvider());	
		configureLangTableViewer.setInput(this.mapping);
		
		container.setEnabled(false);
		setControl(containerCheck);
		setPageComplete(true);
	}

	private void buttonChangeModellingLang(final Composite container) {
		WidgetFactory.button(SWT.NONE).text("Add Modeling Language").create(container).addListener(SWT.Selection, event -> {
			final RegisteredPackageDialog registeredPackageDialog = new RegisteredPackageDialog(getShell());
			  registeredPackageDialog.setMultipleSelection(false);
			  registeredPackageDialog.open();
			  Object [] result = registeredPackageDialog.getResult();
              if (result != null && result.length == 1) {
            	  final String sourceNsUris = (String) result[0];
            	  if (registeredPackageDialog.isDevelopmentTimeVersion()) {
            		  Map<String, URI> ePackageNsURItoGenModelLocationMap = EcorePlugin.getEPackageNsURIToGenModelLocationMap(false);       		  
            		  URI location = ePackageNsURItoGenModelLocationMap.get(sourceNsUris);
            		  ResourceSet reset = new ResourceSetImpl();
            		  Resource genModelResource = reset.getResource(location, true);
            		  final GenModel genModel = (GenModel) genModelResource.getContents().get(0);            		  
            		  this.mapping.setGenModel(genModel);
            		  labelNsUri.setText("Mapping to Language: " + sourceNsUris);
            		  container.layout();
            		  tableComposite.layout();
            		  configureLangTableViewer.refresh();
            		  configureLangTableViewer.getTree().layout(); 
            		  ((Composite)getControl()).layout();
            	  }
              }              
		});
	}
	
	private Listener enableListener(Composite composite) {
		return new Listener() {			
			@Override
			public void handleEvent(Event event) {
				 if (event.widget instanceof Button) {
					final Button button = (Button) event.widget;
					if (button.getSelection())
						composite.setEnabled(true);
					else
						composite.setEnabled(false);
				}									
			}
		};		
	}

	private void createColumns() {
		//Target and Items (Source Language)
		TreeViewerColumn srcLanguageColumn = new TreeViewerColumn(this.configureLangTableViewer, SWT.LEFT);
		srcLanguageColumn.getColumn().setWidth(180);
		srcLanguageColumn.getColumn().setText("Source Language - Target and Items");
		
		//Provider Target and Items from the Source Language
		srcLanguageColumn.setLabelProvider(new MLSourceLanguageProvider());
		
		//Target and Items (Target Language)
		TreeViewerColumn targetLanguageColumn = new TreeViewerColumn(this.configureLangTableViewer, SWT.LEFT);
		targetLanguageColumn.getColumn().setWidth(180);
		targetLanguageColumn.getColumn().setText("Target Language - Target and Items");
		targetLanguageColumn.setEditingSupport(new EditingTargetLangElements(configureLangTableViewer,this.mapping));
		
		//Provide Target and Items from the Target Language
		targetLanguageColumn.setLabelProvider(new MLTargetLanguageProvider());
		
		//Target and Items (Target Language)
		TreeViewerColumn writeSpecFeatureColumn = new TreeViewerColumn(this.configureLangTableViewer, SWT.LEFT);
		writeSpecFeatureColumn.getColumn().setWidth(180);
		writeSpecFeatureColumn.getColumn().setText("Target Language - Write Feature");
		writeSpecFeatureColumn.setEditingSupport(new EditingTargetWriteLangElements(configureLangTableViewer, this.mapping));
		
		//Specify the write features
		writeSpecFeatureColumn.setLabelProvider(new MLWriteFeatureProvider());
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.mapping.setSourceToTargetMap(getSourcetoTargetMap());
		
		configureLangTableViewer.refresh();
		((Composite)getControl()).layout();
	}
	
	@Override
	public IronManWizard getWizard() {
		return (IronManWizard) super.getWizard();
	}
	
	private Map<MapTargetElement, List<MapItemElement>> getSourcetoTargetMap() {
		final Map<String, List<Recommender>> selectedServerToRecommender 
							= getWizard().getSelectedServerToRecommender();
		final Map<MapTargetElement, List<MapItemElement>> sourceToTargetMap = new 
				LinkedHashMap<MapTargetElement, List<MapItemElement>>();
		for (Entry<String, List<Recommender>> entryRecommender : selectedServerToRecommender.entrySet()) {
			final List<Recommender> recommenders = entryRecommender.getValue();
			for (Recommender recommender : recommenders) {
				MapTargetElement entryTargetElement = 
						isTargetPresent(sourceToTargetMap, recommender.getDetails().getTarget()); 
				if (entryTargetElement == null) {	
					entryTargetElement = new MapTargetElement(recommender.getDetails().getTarget(), null);
					sourceToTargetMap.put(entryTargetElement, new ArrayList<MapItemElement>());
				}
				for(String item: recommender.getDetails().getItems()) {
					final boolean isItemPresent = isItemPresent(sourceToTargetMap.get(entryTargetElement), item); 
					if (!isItemPresent) {					
						final MapItemElement itemElement = new MapItemElement(item, null, null);
						sourceToTargetMap.get(entryTargetElement).add(itemElement);						
					}					
				}
			}			
		}		
		return sourceToTargetMap;
	}
	
	private MapTargetElement isTargetPresent(final Map<MapTargetElement, List<MapItemElement>> sourceToTargetMap, String target) {
		 final Optional<Entry<MapTargetElement, List<MapItemElement>>> targetElement = sourceToTargetMap.entrySet().stream()
			 .filter(e -> e.getKey().getSourceElement().equals(target))
			 .findAny();
		 if (targetElement.isPresent())
			 return targetElement.get().getKey(); 
		 return null;		
	}
	
	private boolean isItemPresent(List<MapItemElement> listOfItems, String item) {
		return listOfItems
					.stream()
					.filter(presentedItem -> presentedItem.getItem().equals(item))
					.findAny()
					.isPresent();	
	}	

}
