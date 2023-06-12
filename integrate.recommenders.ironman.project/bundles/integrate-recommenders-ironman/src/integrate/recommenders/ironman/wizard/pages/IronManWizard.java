package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class IronManWizard extends Wizard implements INewWizard {
	
	//Wizard Pages
	private SelectRecommenders selectRec;
	private SelectTargetItems selectTargetItems;
	private ConfigureModellingLanguage modellingLanguages;
	private ConfigureAggregationMethod aggMethod;
	private ConfigureModellingTools modellingTools;
	
	//Wizard Pages's Name
	public static final String SELECT_RECOMMENDER_PAGE_NAME = "selRecommender"; 
	
	public static final String SELECT_TARGET_ITEM_PAGE_NAME = "selTargetItems";
	
	public static final String SELECT_MODELLING_LANGUAGE_PAGE_NAME = "configLanguage"; 
	
	public static final String SELECT_AGGREGATION_METHOD_PAGE_NAME = "selAggMethod";
	
	public static final String SELECT_MODELLING_TOOLS_PAGE_NAME = "modeToolsIntegration";

	private final Map<String,List<Service>> recommenderToServices;
	
	public IronManWizard() {
		setWindowTitle(IRONMAN_WIZARD_NAME);
		//Get All Recommenders
		recommenderToServices = getAllRecommender();
	}
	
	@Override
	public void addPages() {
		// Select Recommender - page 1
		selectRec = new SelectRecommenders(SELECT_RECOMMENDER_PAGE_NAME);
		//Select Target & Items
		selectTargetItems = new SelectTargetItems(SELECT_TARGET_ITEM_PAGE_NAME);
		//Configure the Mapping to another modelling language
		modellingLanguages = new ConfigureModellingLanguage(SELECT_MODELLING_LANGUAGE_PAGE_NAME);
		//Select Aggregation Method
		aggMethod = new ConfigureAggregationMethod(SELECT_AGGREGATION_METHOD_PAGE_NAME);
		// Select Modelling Tools
		modellingTools = new ConfigureModellingTools(SELECT_MODELLING_TOOLS_PAGE_NAME);
		
		addPage(selectRec);
		addPage(selectTargetItems);
		addPage(modellingLanguages);
		addPage(aggMethod);
		addPage(modellingTools);
	}
	
	public Map<String,List<Service>> getAllRecommenders() {
		return recommenderToServices;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MLMappingConfiguration getMappingConfiguration() {
		return this.modellingLanguages.getMapping();
	}
	
	public Map<String,List<Recommender>> getSelectedServerToRecommender() {
		return selectTargetItems.getSelectedServerToRecommender();
	}
	
	//Return a map with key = URL of the server value = all selected recommenders
	public Map<String,List<Service>> getAllSelectedRecommenders() {
		final Object[] allObjects = selectRec.getCheckboxTreeViewer().getCheckedElements();
		final Map<String,List<Service>> mapServersToServices = new HashMap<String, List<Service>>();
		String server = null;
		Service service = null;
		for (Object object : allObjects) {
			if (object instanceof Map.Entry) {
				final Entry<?, ?> serversToServices = (Map.Entry<?, ?>) object;
				server = (String) serversToServices.getKey();	
				mapServersToServices.put(server, List.of());
			} else if (object instanceof Service) {
				final List<Service> services = mapServersToServices.get(server);
				final List<Service> newServices = services
						.stream()
						.collect(Collectors.toList());
				newServices.add((Service)object);	
				service = newServices.get(newServices.size() - 1);
				service.getServices().clear();
				mapServersToServices.put(server, newServices);		
			} else if (object instanceof Recommender) {
				service.getServices().add((Recommender)object);
				service.getServices().get(service.getServices().size() - 1).getDetails().setItems(null);
			}
		}	
		addListOfServices(mapServersToServices);
		return mapServersToServices;
	}
	
	//Return a map with key = URL of the server value = all selected recommenders
	private void addListOfServices(Map<String, List<Service>> mapServersToServices) {
		// All the selected target and Items
		final Object[] allObjects = selectTargetItems.getCheckboxTreeViewer().getCheckedElements();
		String server = null;
		String recommenderName = null;
		for (Object object : allObjects) {
			if (object instanceof Map.Entry) {
				server = (String) ((Map.Entry<?,?>) object).getKey();
			} else if (object instanceof Recommender) {
				recommenderName = ((Recommender) object).getName();
			} else if (object instanceof String) {
				final String recommenderNameFinal = recommenderName;
				final Recommender recommender = mapServersToServices
													.get(server)
													.stream()
													.flatMap(s -> s.getServices().stream())
													.filter(r -> r.getName().equals(recommenderNameFinal))
													.findAny()
													.orElseThrow(() -> new IllegalArgumentException("Cannot find recommended name: " + recommenderNameFinal))
													;
				if (recommender.getDetails().getItems() == null) {
					recommender.getDetails().setItems(new ArrayList<String>());
				} 
				recommender.getDetails().getItems().add((String)object);				
			}
		}		
	}
}
