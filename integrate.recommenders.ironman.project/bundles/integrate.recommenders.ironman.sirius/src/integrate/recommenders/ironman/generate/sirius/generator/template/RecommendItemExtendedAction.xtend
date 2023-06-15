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
						//TODO EMF Jackson convert from XMI to JSON
						final RecommenderCase recommenderCase = getRecommenderCase("{name: Author}");
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
			import java.util.List;
			import org.eclipse.jface.window.Window;
			import org.eclipse.ui.PlatformUI;
			import java.util.Map;
			import java.util.AbstractMap;
			import integrate.recommenders.ironman.definition.algorithm.EvaluateMetaSearchContributionHandler;
			import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
			import «GenModelUtils.getPackageClassFromEClass(this.recommender.details.targetEClass)»;
			import «this.packageNameUtils».RecommenderCase;
			import static «this.packageNameUtils».RecommenderUtils.*;
			import java.util.Arrays;
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
								«allRecommenders»
						);
				final String type = "«getEType»";
				return new RecommenderCase(urlToRecommenders, type, targetName);
			}
		'''
	}
	
	def String getEType() {
		val struct = this.recommender.details.targetEClass.getEStructuralFeature(item);
		struct.EType.name;		
	}
	
	def String allRecommenders(){
		'''
		«var int i = 1»
		«FOR Map.Entry<String, List<Service>> entryRecommend : recommenderToServices.entrySet»
			«IF recommendTypeOfItem(entryRecommend)»
				URL_RECOMMENDER_«i++»,
					Arrays.asList(
					//List of Recommenders
					«FOR Service service: entryRecommend.value»
						«FOR Recommender recommender: service.services SEPARATOR ','»
						RECOMMENDER_«i»_«recommender.name»
						«ENDFOR»
					«ENDFOR»
					))
			«ELSE»
			«{i=i+1;null}»
			«ENDIF»
		«ENDFOR»		
		'''
	}
	
	def boolean recommendTypeOfItem(Map.Entry<String, List<Service>> entryRecommend) {
		return entryRecommend.value.stream.filter(serv | 
			serv.services.stream.filter( r | r.details.items.contains(this.item)).findFirst.isEmpty === false		
		).count > 0;	
	}	
}
