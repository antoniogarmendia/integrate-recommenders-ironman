package ecore.tree.integration;		

import java.util.Arrays;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
		
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import integrate.recommenders.ironman.definition.algorithm.EvaluateMetaSearchContributionHandler;
import integrate.recommenders.ironman.definition.recommendation.Recommendation;
import integrate.recommenders.ironman.definition.recommendation.RecommendationRequest;
import integrate.recommenders.ironman.definition.recommendation.SpecAttribute;
import integrate.recommenders.ironman.definition.recommendation.Target;
import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import static ecore.tree.integration.RecommenderUtils.*;

public class RecommenderPopup extends ExtensionContributionFactory {
		public RecommenderPopup() {
				// Do nothing
		}		
		
		@Override
		public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
			ISelectionService serv = (ISelectionService) serviceLocator.getService(ISelectionService.class);
			ISelection selection = serv.getSelection();
			//Get first element
			if (selection instanceof TreeSelection) {
	
				TreeSelection treeselection = (TreeSelection)selection;
				Object firstelement = treeselection.getFirstElement();
				//Cast of the target element
				if (firstelement instanceof EClass) {
					//Add Recommender Menu
					MenuManager menu = new MenuManager();
					menu.setMenuText("Recommender");
					menu.add(menueAllAttributes((EClass)firstelement));
					menu.add(menueAllOperations((EClass)firstelement));
					additions.addContributionItem(menu, null);				
				}
			}
		}
					
		public Action menueAllAttributes(EClass target) {
			return new Action("Recommend EAttribute",null) {		
								@Override
								public void run() {
								
								final EClassifier eClassifier = target.eClass().getEStructuralFeature("eAllAttributes").getEType();
								final EStructuralFeature struct = target.eClass().getEStructuralFeature("name");
								final String value = target.eGet(struct).toString();									
											
								final RecommenderCase recommenderCase = getRecommenderCaseeAllAttributes(value,eClassifier,target);
								final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
									//Merge 
								final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
										executeMetaSearchAlgorithByName("BordaCount", recServerToItemRecommenders);
								if (dataFusion.size() != 0 ) {
									final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
									//Graphical Interface
									final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
											, recServerToItemRecommenders, normalizeDataFusion, "EClass", eClassifier.getName());
													
									if (recDialog.open() == Window.OK) {
										//Add selected elements
										addSelectRecommendationeAllAttributes(recDialog.getSelectedRecommendations(),target);
									}
								} else {
									final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										                SWT.ICON_INFORMATION |
										                SWT.OK);
									messageBox.setMessage("It was not provided any recommendation");
									messageBox.open();
								}
					}
			};
		}
		public Action menueAllOperations(EClass target) {
			return new Action("Recommend EOperation",null) {		
								@Override
								public void run() {
								
								final EClassifier eClassifier = target.eClass().getEStructuralFeature("eAllAttributes").getEType();
								final EStructuralFeature struct = target.eClass().getEStructuralFeature("name");
								final String value = target.eGet(struct).toString();									
											
								final RecommenderCase recommenderCase = getRecommenderCaseeAllOperations(value,eClassifier,target);
								final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
									//Merge 
								final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
										executeMetaSearchAlgorithByName("BordaCount", recServerToItemRecommenders);
								if (dataFusion.size() != 0 ) {
									final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
									//Graphical Interface
									final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
											, recServerToItemRecommenders, normalizeDataFusion, "EClass", eClassifier.getName());
													
									if (recDialog.open() == Window.OK) {
										//Add selected elements
										addSelectRecommendationeAllOperations(recDialog.getSelectedRecommendations(),target);
									}
								} else {
									final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										                SWT.ICON_INFORMATION |
										                SWT.OK);
									messageBox.setMessage("It was not provided any recommendation");
									messageBox.open();
								}
					}
			};
		}
		private RecommenderCase getRecommenderCaseeAllAttributes(String targetName, EClassifier eClassifier,
				 EClass target) {
			final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
					new AbstractMap.SimpleEntry<String, Collection<String>>(
								URL_RECOMMENDER_1,
									Arrays.asList(
									//List of Recommenders
									RECOMMENDER_2_MetamodelRecommender006,						
									RECOMMENDER_2_MetamodelRecommender005,						
									RECOMMENDER_2_MetamodelRecommender007,						
									RECOMMENDER_2_MetamodelRecommender002,						
									RECOMMENDER_2_MetamodelRecommender001,						
									RECOMMENDER_2_MetamodelRecommender004,						
									RECOMMENDER_2_MetamodelRecommender003						
										))
					);
			final String body = makeBodyeAllAttributes(target, targetName);
			final String type = eClassifier.getName();
			return new RecommenderCase(urlToRecommenders, type, body);
		}
		private Target getTargeteAllAttributes(EClass target, String targetName) {
			final Target targetRequest = new Target();
			targetRequest.setName(targetName);
			final EStructuralFeature readStruct = target.eClass().getEStructuralFeature("eAllAttributes");
			final EStructuralFeature getStructValue = target.eClass().getEStructuralFeature("name");
			
			@SuppressWarnings("unchecked")
			EList<EObject> listOfElements =  (EList<EObject>) target.eGet(readStruct);
			for (EObject eObject : listOfElements) {
				final SpecAttribute specAttribute = new SpecAttribute();
				specAttribute.setName(eObject.eGet(getStructValue).toString());
				targetRequest.getEAttributes().add(specAttribute);
			}		
			return targetRequest;
		}
		private String makeBodyeAllAttributes(EClass target, String targetName) {
			final RecommendationRequest recRequest = new RecommendationRequest();
			final Recommendation recommendation = new Recommendation();
			recRequest.setRecommendation(recommendation);
			recommendation.setTarget(getTargeteAllAttributes(target,targetName));
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				final String result = objectMapper.writeValueAsString(recRequest);
				return result;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}		
			return null;
		}	
		
		private void addSelectRecommendationeAllAttributes(List<RecommenderData> selectedRecommendations, 
			EClass target) {
			selectedRecommendations.stream().forEach(rec -> {
				final EClassifier eClassifierRead = target.eClass().getEStructuralFeature("eAllAttributes").getEType();
				final EStructuralFeature structFeature = ((EClass) eClassifierRead).getEStructuralFeature("name");
				final EObject element = EcoreUtil.create((EClass)eClassifierRead);
				
				//eSet Feature
				element.eSet(structFeature, rec.getName());
				
				//Write
				EStructuralFeature structFeateAllAttributes = target.eClass().getEStructuralFeature("eAttributes");
				@SuppressWarnings("unchecked")
				EList<EObject> listOfAttributes =  (EList<EObject>) target.eGet(structFeateAllAttributes);
				listOfAttributes.add(element);
				System.out.println("It does work");
			});
		}
		private RecommenderCase getRecommenderCaseeAllOperations(String targetName, EClassifier eClassifier,
				 EClass target) {
			final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
					new AbstractMap.SimpleEntry<String, Collection<String>>(
								URL_RECOMMENDER_1,
									Arrays.asList(
									//List of Recommenders
									RECOMMENDER_2_MetamodelRecommender006,						
									RECOMMENDER_2_MetamodelRecommender005,						
									RECOMMENDER_2_MetamodelRecommender007,						
									RECOMMENDER_2_MetamodelRecommender002,						
									RECOMMENDER_2_MetamodelRecommender001,						
									RECOMMENDER_2_MetamodelRecommender004,						
									RECOMMENDER_2_MetamodelRecommender003						
										))
					);
			final String body = makeBodyeAllOperations(target, targetName);
			final String type = eClassifier.getName();
			return new RecommenderCase(urlToRecommenders, type, body);
		}
		private Target getTargeteAllOperations(EClass target, String targetName) {
			final Target targetRequest = new Target();
			targetRequest.setName(targetName);
			final EStructuralFeature readStruct = target.eClass().getEStructuralFeature("eAllOperations");
			final EStructuralFeature getStructValue = target.eClass().getEStructuralFeature("name");
			
			@SuppressWarnings("unchecked")
			EList<EObject> listOfElements =  (EList<EObject>) target.eGet(readStruct);
			for (EObject eObject : listOfElements) {
				final SpecAttribute specAttribute = new SpecAttribute();
				specAttribute.setName(eObject.eGet(getStructValue).toString());
				targetRequest.getEAttributes().add(specAttribute);
			}		
			return targetRequest;
		}
		private String makeBodyeAllOperations(EClass target, String targetName) {
			final RecommendationRequest recRequest = new RecommendationRequest();
			final Recommendation recommendation = new Recommendation();
			recRequest.setRecommendation(recommendation);
			recommendation.setTarget(getTargeteAllOperations(target,targetName));
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				final String result = objectMapper.writeValueAsString(recRequest);
				return result;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}		
			return null;
		}	
		
		private void addSelectRecommendationeAllOperations(List<RecommenderData> selectedRecommendations, 
			EClass target) {
			selectedRecommendations.stream().forEach(rec -> {
				final EClassifier eClassifierRead = target.eClass().getEStructuralFeature("eAllOperations").getEType();
				final EStructuralFeature structFeature = ((EClass) eClassifierRead).getEStructuralFeature("name");
				final EObject element = EcoreUtil.create((EClass)eClassifierRead);
				
				//eSet Feature
				element.eSet(structFeature, rec.getName());
				
				//Write
				EStructuralFeature structFeateAllAttributes = target.eClass().getEStructuralFeature("eOperations");
				@SuppressWarnings("unchecked")
				EList<EObject> listOfAttributes =  (EList<EObject>) target.eGet(structFeateAllAttributes);
				listOfAttributes.add(element);
				System.out.println("It does work");
			});
		}
}

