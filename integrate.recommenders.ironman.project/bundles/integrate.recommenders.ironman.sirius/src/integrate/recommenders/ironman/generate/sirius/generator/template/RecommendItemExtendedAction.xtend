package integrate.recommenders.ironman.generate.sirius.generator.template

import java.util.List
import java.util.Map
import project.generator.api.template.sirius.ExternalJavaActionTemplate
import integrate.recommenders.ironman.definition.services.Service
import java.util.stream.Collectors
import integrate.recommenders.ironman.definition.services.Recommender
import project.generator.api.utils.GenModelUtils

class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
	
	val Map<String,List<Service>> recommenderToServices;
	val String item;
	val Map.Entry<String, List<Service>> service;
	var Recommender recommender;
	val String packageNameUtils;
	
	new(String className, String packageName, String packageNameUtils, String item, Map<String,List<Service>> recommenderToServices) {
		super(className,packageName);
		this.recommenderToServices = recommenderToServices;
		this.item = item;
		this.service = getService;
		this.packageNameUtils = packageNameUtils;
	}
	
	//Return the Entry
	def getService() {
		return this.recommenderToServices.entrySet.stream.filter(listOfServ | 
				listOfServ.value.stream.filter(serv | 
					existRecommender(serv)
				).count > 0
		).collect(Collectors.toList()).get(0);
	}
	
	//Return the recommender
	def boolean existRecommender(Service service) {
		val rec = service.services.stream.filter( r | r.details.items.contains(this.item)).findAny.orElse(null);
		if (rec !== null) {
			this.recommender = rec;	
			return true;
		}	
		return false;		
	}
	
	override middleDefaultExecute() {
		'''
			if (!selectedElements.isEmpty()) {
				final EObject selectedNode = selectedElements.stream().findFirst().get();
				if (selectedNode instanceof DNodeList) {
					var nodeList = (DNodeList) selectedNode;
					final EObject targetEObject = nodeList.getTarget();
					if (targetEObject instanceof «this.recommender.details.targetEClass.name») {
						final «this.recommender.details.targetEClass.name» target = ((EClass) targetEObject);
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
	
	override importDependencies() {
		'''
			«super.importDependencies()»
			import java.util.AbstractMap;
			import «GenModelUtils.getPackageClassFromEClass(this.recommender.details.targetEClass)»;
			import «this.packageNameUtils».RecommenderCase;
		'''
	}
	
	override restOfMethods() {
		'''
		«recommenderCase»
		'''
	}
	
	def recommenderCase() {
		'''
			private RecommenderCase getRecommenderCase(String targetName) {
				final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
						new AbstractMap.SimpleEntry<String, Collection<String>>(
								URL_RECOMMENDER_1,
								Arrays.asList(
										RECOMMENDER_1,RECOMMENDER_2,RECOMMENDER_3,RECOMMENDER_4
										)	
								)
						);
				final String type = "EAttribute";
				return new RecommenderCase(urlToRecommenders, type, targetName);
			}
		'''
	}
	
}
