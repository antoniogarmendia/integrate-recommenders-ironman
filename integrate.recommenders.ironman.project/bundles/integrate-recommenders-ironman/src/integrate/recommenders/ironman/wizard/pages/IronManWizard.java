package integrate.recommenders.ironman.wizard.pages;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import static integrate.recommenders.ironman.wizard.utils.IronManWizardUtils.*;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;

import java.util.List;
import java.util.Map;

public class IronManWizard extends Wizard implements INewWizard {
	
	//Wizard Pages
	private SelectRecommenders selectRec;
	private SelectTargetItems selectTargetItems;
	private ConfigureModellingLanguage modellingLanguages;
	
	//Wizard Pages's Name
	public static final String SELECT_RECOMMENDER_PAGE_NAME = "selRecommender"; 
	
	public static final String SELECT_TARGET_ITEM_PAGE_NAME = "selTargetItems";
	
	public static final String SELECT_MODELLING_LANGUAGE_PAGE_NAME = "configLanguage"; 

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
		
		addPage(selectRec);
		addPage(selectTargetItems);
		addPage(modellingLanguages);
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
	
	public Map<String,List<Recommender>> getSelectedServerToRecommender() {
		return selectTargetItems.getSelectedServerToRecommender();
	}

}
