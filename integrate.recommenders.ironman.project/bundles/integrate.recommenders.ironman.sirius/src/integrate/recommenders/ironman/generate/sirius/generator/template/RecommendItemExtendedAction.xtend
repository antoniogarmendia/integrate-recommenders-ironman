package integrate.recommenders.ironman.generate.sirius.generator.template

import java.util.List
import java.util.Map
import project.generator.api.template.sirius.ExternalJavaActionTemplate
import integrate.recommenders.ironman.definition.services.Service
import project.generator.api.utils.GenModelUtils
import java.util.stream.Collectors
import org.eclipse.emf.ecore.EClassifier
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration
import integrate.recommenders.ironman.definition.services.Item

class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
	
	val Map<String,List<Service>> recommenderToServices;
	val Item item;
	val List<Service> services;
	val String packageNameUtils;
	val String packageNameDialog;
	val String dataFusionAlgorithm;
	val MLMappingConfiguration mapping;
	
	new(String className, String packageName, String packageNameUtils, String packageNameDialog, Item item, 
		Map<String,List<Service>> recommenderToServices, String dataFusionAlgorithm, MLMappingConfiguration mapping,
		List<Service> services
	) {
		super(className,packageName);
		this.recommenderToServices = recommenderToServices;
		this.item = item;
		this.services = services;
		this.packageNameUtils = packageNameUtils;
		this.packageNameDialog = packageNameDialog;
		this.dataFusionAlgorithm = dataFusionAlgorithm;
		this.mapping = mapping;
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
		val rec = service.detail.items.stream.filter(i | i.read.equals(this.item)).findAny.orElse(null);
		if (rec !== null) {
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
					if (targetEObject instanceof «this.services.get(0).detail.targetEClass.name») {
						final «this.services.get(0).detail.targetEClass.name» target = ((«this.services.get(0).detail.targetEClass.name») targetEObject);
						//TODO EMF Jackson convert from XMI to JSON
						//TODO feature read
						final RecommenderCase recommenderCase = getRecommenderCase(target.getName());
						final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
						//Merge 
						final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
								executeMetaSearchAlgorithByName("«this.dataFusionAlgorithm»", recServerToItemRecommenders);
						if (dataFusion.size() != 0 ) {
							final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
							//Graphical Interface
							final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
									, recServerToItemRecommenders, normalizeDataFusion, "EClass");
							
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
		'''
	}
	
	override importDependencies() {
		'''
			«super.importDependencies()»
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
			import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
			import «GenModelUtils.getPackageClassFromEClassifier(this.getEType)»;
			import «this.packageNameUtils».RecommenderCase;
			import «this.packageNameDialog».RecommenderData;
			import static «this.packageNameUtils».RecommenderUtils.*;
			import java.util.Arrays;
			import «this.packageNameDialog».RecommenderDialog;
			import org.eclipse.swt.SWT;
			import org.eclipse.swt.widgets.MessageBox;			
		'''
	}
	
	override restOfMethods() {
		'''
		«recommenderCase»
		«addSelectedRecommendation»
		'''
	}
	
	def addSelectedRecommendation() {
		'''
		
		private void addSelectRecommendation(List<RecommenderData> selectedRecommendations, 
			«this.services.get(0).detail.targetEClass.name» target) {
			selectedRecommendations.stream().forEach(rec -> {
				final EClassifier classifier = target.eClass().getEPackage().getEClassifier("«getEType.name»");
				final «getEType.name» element = («getEType.name») EcoreUtil.create((EClass)classifier);
				
				//TODO Feature
				element.setName(rec.getName());
				
				//TODO Write
				EStructuralFeature structFeateAllAttributes = target.eClass().getEStructuralFeature("eStructuralFeatures");
				@SuppressWarnings("unchecked")
				EList<EObject> listOfAttributes =  (EList<EObject>) target.eGet(structFeateAllAttributes);
				listOfAttributes.add(element);
				System.out.println("It does work");
			});
		}
		'''
	}
	
	def recommenderCase() {
		'''
			private RecommenderCase getRecommenderCase(String targetName) {
				final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
						new AbstractMap.SimpleEntry<String, Collection<String>>(
								«allRecommenders»
						);
				final String type = "«getEType.name»";
				return new RecommenderCase(urlToRecommenders, type, targetName);
			}
		'''
	}
	
	def EClassifier getEType() {
		this.services.get(0).detail.targetEClass;			
	}
	
	def String allRecommenders(){
		'''
		«var int i = 1»
			URL_RECOMMENDER_«i++»,
				Arrays.asList(
				//List of Recommenders
				«FOR Service service: services SEPARATOR ','»
					RECOMMENDER_«i»_«service.name»						
				«ENDFOR»
					))
		'''
	}
	
	def boolean recommendTypeOfItem(Map.Entry<String, List<Service>> entryRecommend) {
//		return entryRecommend.value.stream.filter(serv | 
//			serv.services.stream.filter( r | r.details.items.contains(this.item)).findFirst.isEmpty === false		
//		).count > 0;
		return false;
	}	
}
