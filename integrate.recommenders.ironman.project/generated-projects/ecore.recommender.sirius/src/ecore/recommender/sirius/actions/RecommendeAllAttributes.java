package ecore.recommender.sirius.actions;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;		
import java.util.List;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.common.util.EList;
import java.util.AbstractMap;
import integrate.recommenders.ironman.definition.algorithm.EvaluateMetaSearchContributionHandler;
import integrate.recommenders.ironman.definition.recommendation.Recommendation;
import integrate.recommenders.ironman.definition.recommendation.RecommendationRequest;
import integrate.recommenders.ironman.definition.recommendation.SpecAttribute;
import integrate.recommenders.ironman.definition.recommendation.Target;
import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
import org.eclipse.emf.ecore.EClass;
import ecore.recommender.sirius.utils.RecommenderCase;
import ecore.recommender.sirius.dialog.RecommenderData;
import static ecore.recommender.sirius.utils.RecommenderUtils.*;
import java.util.Arrays;
import ecore.recommender.sirius.dialog.RecommenderDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;		
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecommendeAllAttributes implements IExternalJavaAction {
	public RecommendeAllAttributes() {
	}
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
	return true;
	}
	@Override
	public void execute(Collection<? extends EObject> selectedElements, Map<String, Object> parameters) {
	if (!selectedElements.isEmpty()) {
		final EObject selectedNode = selectedElements.stream().findFirst().get();
		if (selectedNode instanceof DNodeList) {
			var nodeList = (DNodeList) selectedNode;
			final EObject targetEObject = nodeList.getTarget();
			if (targetEObject instanceof EClass) {
				final EClass target = ((EClass) targetEObject);
				final EClassifier eClassifier = target.eClass().getEStructuralFeature("eAllAttributes").getEType();
				final EStructuralFeature struct = target.eClass().getEStructuralFeature("name");
				final String value = target.eGet(struct).toString();									
				
				final RecommenderCase recommenderCase = getRecommenderCase(value,eClassifier,target);
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
						addSelectRecommendation(recDialog.getSelectedRecommendations(),target);
					}
				} else {
					final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												                SWT.ICON_INFORMATION |
												                SWT.OK);
					messageBox.setMessage("It was not provided any recommendation");
					messageBox.open();
				}				
			}				
		}			
	}	
	}
	private RecommenderCase getRecommenderCase(String targetName, EClassifier eClassifier, EClass target) {
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
		final String body = makeBody(target, targetName);
		final String type = eClassifier.getName();
		return new RecommenderCase(urlToRecommenders, type, body);
	}
	
	private void addSelectRecommendation(List<RecommenderData> selectedRecommendations, 
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
	
	private String makeBody(EClass target, String targetName) {
		final RecommendationRequest recRequest = new RecommendationRequest();
		final Recommendation recommendation = new Recommendation();
		recRequest.setRecommendation(recommendation);
		recommendation.setTarget(getTarget(target,targetName));
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final String result = objectMapper.writeValueAsString(recRequest);
			return result;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		
		return null;
	}		
	private Target getTarget(EClass target, String targetName) {
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
}		
