package integrate.recommenders.ironman.generate.sirius.generator.template

import java.util.List
import java.util.Map
import project.generator.api.template.sirius.ExternalJavaActionTemplate
import integrate.recommenders.ironman.definition.services.Service

class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
	
	val Map<String,List<Service>> recommenderToServices;
		
	new(String className, String packageName, Map<String,List<Service>> recommenderToServices) {
		super(className,packageName);
		this.recommenderToServices = recommenderToServices;
	}
	
	override middleDefaultExecute() {
		'''
			if (!selectedElements.isEmpty()) {
				final EObject selectedNode = selectedElements.stream().findFirst().get();
				if (selectedNode instanceof DNodeList) {
					var nodeList = (DNodeList) selectedNode;
					final EObject targetEObject = nodeList.getTarget();
					//TODO generate target EClass or Class
					if (targetEObject instanceof EClass) {
						//TODO EClass name is not a reflective...
						final EClass eClass = ((EClass) targetEObject);
						final String name = eClass.getName();
						//TODO Call all the recommenders
						final RecommenderCase recommenderCase = getRecommenderCase(name);
						final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
						//Merge 
						final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
								executeMetaSearchAlgorithByName("OutRankingApproach", recServerToItemRecommenders);
						final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
						//Graphical Interface
						final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
								, recServerToItemRecommenders, normalizeDataFusion, "EClass");
						
						if (recDialog.open() == Window.OK) {
							//Add selected elements
							addSelectRecommendation(recDialog.getSelectedRecommendations(),eClass);
						}					
					}				
				}			
			}	
		'''
	}
	
}
