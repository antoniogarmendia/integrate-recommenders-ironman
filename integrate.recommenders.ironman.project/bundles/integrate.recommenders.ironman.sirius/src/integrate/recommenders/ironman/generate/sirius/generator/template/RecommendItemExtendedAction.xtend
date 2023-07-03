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
import integrate.recommenders.ironman.definition.mapping.TargetItemElement
import integrate.recommenders.ironman.definition.mapping.TargetElement

class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
	
	val Map<String,List<Service>> recommenderToServices;
	val Item item;
	val List<Service> services;
	val String packageNameUtils;
	val String packageNameDialog;
	val String dataFusionAlgorithm;
	val MLMappingConfiguration mapping;
	var TargetElement targetElement;
	var TargetItemElement targetItemElement;
	
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
		if (this.mapping !== null) {
			this.targetElement = getTargetElement();
			this.targetItemElement = getTargetItemElement();
		}			
	}
	
	def TargetElement getTargetElement() {		
		return mapping.mapTargetElementToTargetItems.entrySet
					.stream.filter(entry | isItemPresent(entry.value,item))
					.findAny
					.get.key
	}
	
	def TargetItemElement getTargetItemElement() {
		return mapping.mapTargetElementToTargetItems
					.get(this.targetElement)
					.stream.filter(i | i.getFeature().getItem().equals(item.getFeatures())
								&& i.getRead().getItem().equals(item.getRead())
								&& i.getWrite().getItem().equals(item.getWrite())
								).findAny()
								.get
	}
	
	def boolean isItemPresent(List<TargetItemElement> list, Item item) {
		println(list.toString + "" + item.read)
		return list.stream().filter(i | i.getFeature().getItem().equals(item.getFeatures())
								&& i.getRead().getItem().equals(item.getRead())
								&& i.getWrite().getItem().equals(item.getWrite())
								).findAny()
								.isPresent();	
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
				if (selectedNode instanceof DSemanticDecorator) {
					var nodeList = (DSemanticDecorator) selectedNode;
					final EObject targetEObject = nodeList.getTarget();
					if (targetEObject instanceof «getTargetInstanceClass») {
						final «getTargetInstanceClass» target = ((«getTargetInstanceClass») targetEObject);
						final EClassifier eClassifier = target.eClass().getEStructuralFeature("«readStructFeature»").getEType();
						final EStructuralFeature struct = target.eClass().getEStructuralFeature("«actualStructFeature»");
						final String value = target.eGet(struct).toString();									
						
						final RecommenderCase recommenderCase = getRecommenderCase(value,eClassifier,target);
						final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
						//Merge 
						final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
								executeMetaSearchAlgorithByName("«this.dataFusionAlgorithm»", recServerToItemRecommenders);
						if (dataFusion.size() != 0 ) {
							final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
							//Graphical Interface
							final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
									, recServerToItemRecommenders, normalizeDataFusion, "«getTargetInstanceClass()»", «classifierTypeRequest»);
							
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
	
	def String getTargetInstanceClass() {
		if (mapping === null) {
			return this.services.get(0).detail.obtainTargetEClass.name;
		} else 
			return this.targetElement.targetElement.name;
	}
	
	def String readStructFeature() {
		if (mapping === null) {
			return item.read;
		} else {
			return this.targetItemElement.read.structFeature.name
		}
	}
	
	def String writeStructFeature() {
		if (mapping === null) {
			return item.write;
		} else {
			return this.targetItemElement.write.structFeature.name
		}
	}
	
	def String actualStructFeature() {
		if (mapping === null) {
			return item.features
		} else {
			return this.targetItemElement.feature.structFeature.name
		}
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
			import org.eclipse.sirius.viewpoint.DSemanticDecorator;
			import org.eclipse.emf.common.util.EList;
			import java.util.AbstractMap;
			import integrate.recommenders.ironman.definition.algorithm.EvaluateMetaSearchContributionHandler;
			import integrate.recommenders.ironman.definition.recommendation.Recommendation;
			import integrate.recommenders.ironman.definition.recommendation.RecommendationRequest;
			import integrate.recommenders.ironman.definition.recommendation.SpecAttribute;
			import integrate.recommenders.ironman.definition.recommendation.Target;
			import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
			import «GenModelUtils.getPackageClassFromEClassifier(this.getEType)»;
			import «this.packageNameUtils».RecommenderCase;
			import «this.packageNameDialog».RecommenderData;
			import static «this.packageNameUtils».RecommenderUtils.*;
			import java.util.Arrays;
			import «this.packageNameDialog».RecommenderDialog;
			import org.eclipse.swt.SWT;
			import org.eclipse.swt.widgets.MessageBox;		
			import com.fasterxml.jackson.core.JsonProcessingException;
			import com.fasterxml.jackson.databind.ObjectMapper;
			«importModellingLanguageClasses»
		'''
	}
	
	def importModellingLanguageClasses() {
		'''
		«IF mapping !== null»
			«IF !this.targetElement.targetElement.name.equals("EClass")»
		import «GenModelUtils.getPackageClassFromEClassifier(this.targetElement.targetElement)»;
			«ENDIF»
		«ENDIF»
		'''
	}
	
	override restOfMethods() {
		'''
		«recommenderCase»
		«addSelectedRecommendation»
		«makeBody»
		«targetToRequest»
		'''
	}
	
	def targetToRequest() {
		'''
		private Target getTarget(«getTargetInstanceClass» target, String targetName) {
			final Target targetRequest = new Target();
			targetRequest.setName(targetName);
			final EStructuralFeature readStruct = target.eClass().getEStructuralFeature("«readStructFeature»");
			final EStructuralFeature getStructValue = target.eClass().getEStructuralFeature("«actualStructFeature»");
			
			@SuppressWarnings("unchecked")
			EList<EObject> listOfElements =  (EList<EObject>) target.eGet(readStruct);
			for (EObject eObject : listOfElements) {
				final SpecAttribute specAttribute = new SpecAttribute();
				specAttribute.setName(eObject.eGet(getStructValue).toString());
				targetRequest.getEAttributes().add(specAttribute);
			}		
			return targetRequest;
		}
		'''
	}
	
	def addSelectedRecommendation() {
		'''
		
		private void addSelectRecommendation(List<RecommenderData> selectedRecommendations, 
			«getTargetInstanceClass» target) {
			selectedRecommendations.stream().forEach(rec -> {
				final EClassifier eClassifierRead = target.eClass().getEStructuralFeature("«readStructFeature»").getEType();
				final EStructuralFeature structFeature = ((EClass) eClassifierRead).getEStructuralFeature("«actualStructFeature»");
				final EObject element = EcoreUtil.create((EClass)eClassifierRead);
				
				//eSet Feature
				element.eSet(structFeature, rec.getName());
				
				//Write
				EStructuralFeature structFeateAllAttributes = target.eClass().getEStructuralFeature("«writeStructFeature»");
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
			private RecommenderCase getRecommenderCase(String targetName, EClassifier eClassifier, «getTargetInstanceClass» target) {
				final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
						new AbstractMap.SimpleEntry<String, Collection<String>>(
								«allRecommenders»
						);
				final String body = makeBody(target, targetName);
				final String type = «classifierTypeRequest»;
				return new RecommenderCase(urlToRecommenders, type, body);
			}
		'''
	}
	
	def classifierTypeRequest() {
		if (mapping === null) {
			return "eClassifier.getName()";
		} else {
<<<<<<< HEAD
			return "\"" + this.targetItemElement.read.item + "\"";
=======
			return "\"" + this.item.className + "\"";
>>>>>>> is-designer2
		}		
	}
	
	def EClassifier getEType() {
<<<<<<< HEAD
		this.services.get(0).detail.obtainTargetEClass;			
=======
		if (this.mapping === null) {
			return this.services.get(0).detail.obtainTargetEClass;
			} else {
				return mapping.mapTargetElementToTargetItems.entrySet.get(0).key.targetElement;					
		}						
>>>>>>> is-designer2
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
	
	def makeBody() {
		'''
		
		private String makeBody(«getTargetInstanceClass» target, String targetName) {
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
		'''
	}
	
	def boolean recommendTypeOfItem(Map.Entry<String, List<Service>> entryRecommend) {
//		return entryRecommend.value.stream.filter(serv | 
//			serv.services.stream.filter( r | r.details.items.contains(this.item)).findFirst.isEmpty === false		
//		).count > 0;
		return false;
	}	
}
