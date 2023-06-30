package integrate.recommenders.ironman.generate.tree.editor.template

import project.generator.api.template.IGeneration
import java.util.List
import integrate.recommenders.ironman.definition.services.Service
import java.util.Map
import java.util.Set
import java.util.HashSet
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.common.util.BasicEList
import project.generator.api.utils.GenModelUtils
import integrate.recommenders.ironman.definition.services.Item
import integrate.recommenders.ironman.definition.services.Detail
import static integrate.recommenders.ironman.generate.tree.editor.utils.TreeEditorUtils.*
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration
import integrate.recommenders.ironman.definition.mapping.TargetElement
import integrate.recommenders.ironman.definition.mapping.TargetItemElement
import java.util.stream.Stream
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock

class RecommenderPopup implements IGeneration {
	
	val String packageName;
	val Map<String, List<Service>> recommenderToServices;
	val Map<Item,List<Service>> itemToService;
	val MLMappingConfiguration mapping;
	val String dataFusionAlgorithm;
		
	new(String packageName, Map<String, List<Service>> recommenderToServices, 
		MLMappingConfiguration mapping, String dataFusionAlgorithm
	) {
		this.packageName = packageName;
		this.recommenderToServices = recommenderToServices;
		this.itemToService = getAllItems(this.recommenderToServices)
		this.mapping = mapping;		
		this.dataFusionAlgorithm = dataFusionAlgorithm;
	}
	
	override doGenerate() {
		'''
		«packDeclarationImports»
		
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
						«FOR EClass target: listOfDifferentTarget»
						if (firstelement instanceof «target.name») {
							//Add Recommender Menu
							MenuManager menu = new MenuManager();
							menu.setMenuText("Recommender");
							«addAllMenus(target.name)»			
							additions.addContributionItem(menu, null);				
						}
						«ENDFOR»					
					}
				}
							
				«menuMethods»
				«recommenderCase»								
		}
		
		'''
	}
	
		def menuMethods() {
		'''	
		«FOR Map.Entry<Item,List<Service>> item : itemToService.entrySet»
		public Action menu«readStructFeature(item.key)»(«getTargetInstanceClass(item.key)» target) {
			return new Action("Recommend «actionName(item.key)»",null) {		
								@Override
								public void run() {
								
								final EClassifier eClassifier = target.eClass().getEStructuralFeature("«readStructFeature(item.key)»").getEType();
								final EStructuralFeature struct = target.eClass().getEStructuralFeature("«actualStructFeature(item.key)»");
								final String value = target.eGet(struct).toString();									
											
								final RecommenderCase recommenderCase = getRecommenderCase«item.key.read»(value,eClassifier,target);
								final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);
									//Merge 
								final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.
										executeMetaSearchAlgorithByName("«this.dataFusionAlgorithm»", recServerToItemRecommenders);
								if (dataFusion.size() != 0 ) {
									final Map<String, Double> normalizeDataFusion = normalization(dataFusion);
									//Graphical Interface
									final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
											, recServerToItemRecommenders, normalizeDataFusion, "«getTargetInstanceClass(item.key)»", «classifierTypeRequest(item.key)»);
													
									if (recDialog.open() == Window.OK) {
										//Add selected elements
										addSelectRecommendation«item.key.read»(recDialog.getSelectedRecommendations(),target);
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
		«ENDFOR»
		'''
	}
	
	def String actionName(Item item) {
		if (this.mapping === null) {
			return item.className;
		} else {
			return getTargetItemElement(item).read.structFeature.EType.name;
		}
	}
	
	def EList<EClass> listOfDifferentTarget() {
		if (mapping === null) {
			val listOfClasses = new BasicEList<EClass>();
			for(Map.Entry<String, List<Service>> service: recommenderToServices.entrySet){
				for (serv: service.value){
					if (!serv.detail.obtainTargetEClass.name.equals("EClass")
						&& !listOfClasses.contains(serv.detail.obtainTargetEClass)
					){
						listOfClasses.add(serv.detail.obtainTargetEClass);
					}
				}			
			}
			return listOfClasses;
		} else {
			return listOfDifferentTargetMapping;
		}	
	}
	
	def EList<EClass> listOfDifferentTargetMapping() {
		val listOfClasses = new BasicEList<EClass>();
		for (Map.Entry<TargetElement,List<TargetItemElement>> entry: mapping.mapTargetElementToTargetItems.entrySet) {
			val TargetElement targetElement = entry.key;
			listOfClasses.add(targetElement.targetElement);
		}		
		return listOfClasses;
	}
	
	def EList<EClass> listOfTargetsEClass(){
		var listOfTargets = new BasicEList<EClass>();
		for (Map.Entry<String, List<Service>> entryService: recommenderToServices.entrySet) {
			for (Service service: entryService.value) {
				if (!listOfTargets.contains(service.detail.obtainTargetEClass))
					listOfTargets.add(service.detail.obtainTargetEClass);
			}
		}		
		return listOfTargets;
	}
	
	def Set<String> getAllTargetItems(String target){
		//Search within the mapping
		val Set<String> allItems = new HashSet<String>();
		if (this.mapping === null) {			
			for (Map.Entry<String, List<Service>> entryService: recommenderToServices.entrySet) {
				for (Service service: entryService.value) {
					if (service.detail.target.equals(target)) {
						for (Item item: service.detail.items) {
							allItems.add(item.read);
						}				
					}
				}
			}			
		} else {
			val List<TargetItemElement> listOfTargetItems = this.mapping.mapTargetElementToTargetItems.entrySet
							.stream.filter(entry | entry.key.targetElement.name.equals(target))
							.map(entry | entry.value)
							.flatMap(list | list.stream)
							.toList
			for (TargetItemElement itemElement : listOfTargetItems) {
				allItems.add(itemElement.read.structFeature.name);
			}			
		}
		return allItems; 
	}
	
	def Detail getTargetItems(String item){
		for (Map.Entry<String, List<Service>> entryService: recommenderToServices.entrySet) {
			for (Service service: entryService.value) {
				for (Item currentItem: service.detail.items) {
						if (currentItem.read.equals(item))
							return service.detail;
				}				
			}
		}		
		return null;
	}
	
	def Set<String> getAllTargetItems(){
		val Set<String> allItems = new HashSet<String>();
		for (Map.Entry<String, List<Service>> entryService: recommenderToServices.entrySet) {
			for (Service service: entryService.value) {
				for (Item item: service.detail.items) {
					allItems.add(item.read);
				}				
			}
		}		
		return allItems;
	}	
	
	def packDeclarationImports() {
		'''
		package «this.packageName»;		
		
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
		
		import org.eclipse.emf.ecore.EClassifier;
		import org.eclipse.emf.common.util.EList;
		import org.eclipse.emf.ecore.EClass;
		import org.eclipse.emf.ecore.EClassifier;
		import org.eclipse.emf.ecore.EObject;
		import org.eclipse.emf.ecore.EStructuralFeature;
		import org.eclipse.emf.ecore.util.EcoreUtil;
		import static «this.packageName».RecommenderUtils.*;
		«importModellingLanguageClasses»
		'''
	}
	
	def importModellingLanguageClasses() {
		'''
		«IF mapping !== null»
			«FOR Map.Entry<Item,List<Service>> item : itemToService.entrySet»
				«IF !this.getTargetElement(item.key).targetElement.name.equals("EClass")»
				import «GenModelUtils.getPackageClassFromEClassifier(this.getTargetElement(item.key).targetElement)»;
				«ENDIF»
			«ENDFOR»
		«ELSE»	
			«FOR EClass target: listOfTargetsEClass»
				import «GenModelUtils.getPackageClassFromEClassifier(target)»;
			«ENDFOR»	
		«ENDIF»
		'''
	}
	
	def addAllMenus(String target) {
		'''
		«FOR item : getAllTargetItems(target)»
		menu.add(menu«item»((«target»)firstelement));
		«ENDFOR»				
		'''
	}
	
	def recommenderCase() {
		'''
			«FOR Map.Entry<Item,List<Service>> item : itemToService.entrySet»
			private RecommenderCase getRecommenderCase«item.key.read»(String targetName, EClassifier eClassifier,
					 «getTargetInstanceClass(item.key)» target) {
				final Map<String, Collection<String>> urlToRecommenders = 	Map.ofEntries(
						new AbstractMap.SimpleEntry<String, Collection<String>>(
								«allRecommenders(item.value)»
						);
				final String body = makeBody«item.key.read»(target, targetName);
				final String type = «classifierTypeRequest(item.key)»;
				return new RecommenderCase(urlToRecommenders, type, body);
			}
			«targetToRequest(getTargetInstanceClass(item.key),item.key)»
			«makeBody(getTargetInstanceClass(item.key),item.key)»
			«addSelectedRecommendation(getTargetInstanceClass(item.key),item.key)»
			«ENDFOR»
		'''
	}
	
	def String getTargetInstanceClass(Item item) {
		if (mapping === null) {
			return itemToService.entrySet.get(0).value.get(0).detail.obtainTargetEClass.name;
		} else 
			return getTargetElement(item).targetElement.name;
	}
	
	def TargetElement getTargetElement(Item item) {
		return mapping.mapTargetElementToTargetItems.entrySet
					.stream.filter(entry | isItemPresent(entry.value,item))
					.findAny
					.get.key
	}
	
	def classifierTypeRequest(Item item) {
		if (mapping === null) {
			return "eClassifier.getName()";
		} else {
			return "\"" + item.className + "\"";
		}		
	}
	
	def boolean isItemPresent(List<TargetItemElement> list, Item item) {
		return list.stream().filter(i | i.getFeature().getItem().equals(item.getFeatures())
								&& i.getRead().getItem().equals(item.getRead())
								&& i.getWrite().getItem().equals(item.getWrite())
								).findAny()
								.isPresent();	
	}
	
	def String allRecommenders(List<Service> services){
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
	
	def makeBody(String targetInstanceEClass, Item item) {
		'''
		private String makeBody«item.read»(«targetInstanceEClass» target, String targetName) {
			final RecommendationRequest recRequest = new RecommendationRequest();
			final Recommendation recommendation = new Recommendation();
			recRequest.setRecommendation(recommendation);
			recommendation.setTarget(getTarget«item.read»(target,targetName));
			
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
	
	def targetToRequest(String targetInstanceEClass, Item item) {
	'''
	private Target getTarget«item.read»(«targetInstanceEClass» target, String targetName) {
		final Target targetRequest = new Target();
		targetRequest.setName(targetName);
		final EStructuralFeature readStruct = target.eClass().getEStructuralFeature("«readStructFeature(item)»");
		final EStructuralFeature getStructValue = target.eClass().getEStructuralFeature("«actualStructFeature(item)»");
		
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
	
	def addSelectedRecommendation(String targetInstanceEClass, Item item) {
		'''
		
		private void addSelectRecommendation«item.read»(List<RecommenderData> selectedRecommendations, 
			«targetInstanceEClass» target) {
			selectedRecommendations.stream().forEach(rec -> {
				final EClassifier eClassifierRead = target.eClass().getEStructuralFeature("«readStructFeature(item)»").getEType();
				final EStructuralFeature structFeature = ((EClass) eClassifierRead).getEStructuralFeature("«actualStructFeature(item)»");
				final EObject element = EcoreUtil.create((EClass)eClassifierRead);
				
				//eSet Feature
				element.eSet(structFeature, rec.getName());
				
				//Write
				EStructuralFeature structFeateAllAttributes = target.eClass().getEStructuralFeature("«writeStructFeature(item)»");
				@SuppressWarnings("unchecked")
				EList<EObject> listOfAttributes =  (EList<EObject>) target.eGet(structFeateAllAttributes);
				listOfAttributes.add(element);
				System.out.println("It does work");
			});
		}
		'''
	}
	
	def TargetItemElement getTargetItemElement(Item item) {
		return mapping.mapTargetElementToTargetItems
					.get(this.getTargetElement(item))
					.stream.filter(i | i.getFeature().getItem().equals(item.getFeatures())
								&& i.getRead().getItem().equals(item.getRead())
								&& i.getWrite().getItem().equals(item.getWrite())
								).findAny()
								.get
	}
	
	def String readStructFeature(Item item) {
		if (mapping === null) {
			return item.read;
		} else {
			return this.getTargetItemElement(item).read.structFeature.name
		}
	}
	
	def String writeStructFeature(Item item) {
		if (mapping === null) {
			return item.read;
		} else {
			return this.getTargetItemElement(item).write.structFeature.name
		}
	}
	
	def String actualStructFeature(Item item) {
		if (mapping === null) {
			return item.features
		} else {
			return this.getTargetItemElement(item).feature.structFeature.name
		}
	}
}