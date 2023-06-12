package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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
import integrate.recommenders.ironman.wizard.pages.label.MLSourceLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.MLTargetLanguageProvider;

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
	private Button mappingLanguageButton;
	private TreeViewer configureLangTreeViewer;
	private Button checkedButton;
		
	protected ConfigureModellingLanguage(String pageName) {
		super(pageName);
		setTitle(IRONMAN_WIZARD_PAGE_CONFIGURE_MODELLING_LANGUAGE);	
		//Get Selected Target & Items
		mapping = new MLMappingConfiguration(null,null);
		this.mappingLanguageButton = null;
		this.checkedButton = null;
	}	

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);		
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());		
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL,GridData.FILL_VERTICAL));
		//Check if mapping is necessary
		this.checkedButton = WidgetFactory.button(SWT.CHECK)
				.text("Configure a Mapping to Another Language")
				.create(container);					
	
		checkedButton.addListener(SWT.Selection, enableListener(container));
		//Create composite to select the mapping to a language
		this.labelNsUri = WidgetFactory.label(SWT.NONE).text("Mapping to Language: [Language not defined]").create(container);
		//Button to Search Package
		buttonChangeModellingLang(container);

		configureLangTreeViewer = new TreeViewer(container, SWT.VIRTUAL | SWT.BORDER );
		configureLangTreeViewer.getTree().setHeaderVisible(true);
		configureLangTreeViewer.getTree().setLinesVisible(true);
		configureLangTreeViewer.setUseHashlookup(true);
		
		configureLangTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL,GridData.FILL, true, true,3,1));
		createColumns();
		
		configureLangTreeViewer.setContentProvider(new MLConfigureLanguageContentProvider());	
		configureLangTreeViewer.setInput(this.mapping);
		
		mappingModellingLanguageOptions(false);
		
		setControl(container);
		setPageComplete(true);
	}

	private void mappingModellingLanguageOptions(boolean enabled) {
		this.configureLangTreeViewer.getTree().setEnabled(enabled);
		this.mappingLanguageButton.setEnabled(enabled);		
	}

	private void buttonChangeModellingLang(final Composite container) {
		this.mappingLanguageButton  = WidgetFactory.button(SWT.NONE).text("Add Modeling Language").create(container);
		this.mappingLanguageButton.addListener(SWT.Selection, event -> {
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
            		  labelNsUri.redraw();            		  
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
					mappingModellingLanguageOptions(button.getSelection());					
				}									
			}
		};		
	}

	private void createColumns() {
		//Target and Items (Source Language)
		TreeViewerColumn srcLanguageColumn = new TreeViewerColumn(this.configureLangTreeViewer, SWT.LEFT);
		srcLanguageColumn.getColumn().setWidth(180);
		srcLanguageColumn.getColumn().setText("Source Language - Target and Items");
		
		//Provider Target and Items from the Source Language
		srcLanguageColumn.setLabelProvider(new MLSourceLanguageProvider());
		
		//Target and Items (Target Language)
		TreeViewerColumn targetLanguageColumn = new TreeViewerColumn(this.configureLangTreeViewer, SWT.LEFT);
		targetLanguageColumn.getColumn().setWidth(220);
		targetLanguageColumn.getColumn().setText("Target Language - Write Feature");
		targetLanguageColumn.setEditingSupport(new EditingTargetLangElements(this.configureLangTreeViewer, this.mapping));
		
		//Provide Target and Items from the Target Language
		targetLanguageColumn.setLabelProvider(new MLTargetLanguageProvider());		
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (this.mapping.getEPackage() == null) {		
			this.mapping.setSourceToTargetMap(getSourcetoTargetMap());
			this.configureLangTreeViewer.refresh();
			((Composite)getControl()).layout();
		}
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
						final MapItemElement itemElement = new MapItemElement(item, null);
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
