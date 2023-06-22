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
import integrate.recommenders.ironman.definition.services.Item;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.wizard.pages.contents.MLConfigureLanguageContentProvider;
import integrate.recommenders.ironman.wizard.pages.editing.EditingFeatureTargetLangElements;
import integrate.recommenders.ironman.wizard.pages.editing.EditingTargetEClassLangElements;
import integrate.recommenders.ironman.wizard.pages.editing.EditingTargetLangElements;
import integrate.recommenders.ironman.wizard.pages.label.MLSourceLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.MLTargetLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.TargetEClassLanguageProvider;
import integrate.recommenders.ironman.wizard.pages.label.TargetFeatureLanguageProvider;

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
		
		//Type EClass column
		TreeViewerColumn typeEClassLanguageColumn = new TreeViewerColumn(this.configureLangTreeViewer, SWT.CENTER);
		typeEClassLanguageColumn.getColumn().setWidth(220);
		typeEClassLanguageColumn.getColumn().setText("Target Language - Type EClass");	
		typeEClassLanguageColumn.setEditingSupport(new EditingTargetEClassLangElements(this.configureLangTreeViewer, this.mapping));
		//Provide the EClass that match with the selected language
		typeEClassLanguageColumn.setLabelProvider(new TargetEClassLanguageProvider());
				
		//Feature Column
		TreeViewerColumn featureLanguageColumn = new TreeViewerColumn(this.configureLangTreeViewer, SWT.CENTER);
		featureLanguageColumn.getColumn().setWidth(220);
		featureLanguageColumn.getColumn().setText("Target Language - Feature");	
		featureLanguageColumn.setEditingSupport(new EditingFeatureTargetLangElements(this.configureLangTreeViewer));
		//Provide Target and Items from the Target Language
		featureLanguageColumn.setLabelProvider(new TargetFeatureLanguageProvider());			
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
		final Map<String, List<Service>> selectedServerToRecommender 
							= getWizard().getSelectedServerToRecommender();
		final Map<MapTargetElement, List<MapItemElement>> sourceToTargetMap = new 
				LinkedHashMap<MapTargetElement, List<MapItemElement>>();
		for (Entry<String, List<Service>> entryRecommender : selectedServerToRecommender.entrySet()) {
			final List<Service> recommenders = entryRecommender.getValue();
			for (Service recommender : recommenders) {
				MapTargetElement entryTargetElement = 
						isTargetPresent(sourceToTargetMap, recommender.getDetail().getTarget()); 
				if (entryTargetElement == null) {	
					entryTargetElement = new MapTargetElement(recommender.getDetail().getTarget(), null);
					sourceToTargetMap.put(entryTargetElement, new ArrayList<MapItemElement>());
				}
				for(Item item: recommender.getDetail().getItems()) {
					final boolean isItemPresent = isItemPresent(sourceToTargetMap.get(entryTargetElement), item.getRead()); 
					if (!isItemPresent) {		
						final MapItemElement itemElement = new MapItemElement(item.getRead(), null);
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
	
	public MLMappingConfiguration getMapping() {
		return this.mapping;
	}	
	
	private boolean isItemPresent(List<MapItemElement> listOfItems, String item) {
		return listOfItems
					.stream()
					.filter(presentedItem -> presentedItem.getItem().equals(item))
					.findAny()
					.isPresent();	
	}	

}
