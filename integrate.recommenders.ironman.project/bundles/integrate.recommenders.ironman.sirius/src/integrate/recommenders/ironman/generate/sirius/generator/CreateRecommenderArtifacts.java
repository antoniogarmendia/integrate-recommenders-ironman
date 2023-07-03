package integrate.recommenders.ironman.generate.sirius.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.MappingBasedDecoration;
import org.eclipse.sirius.viewpoint.description.Group;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.TargetItemElement;
import integrate.recommenders.ironman.definition.services.Item;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.api.IStrategyGenerateMenu;
import integrate.recommenders.ironman.generate.sirius.generator.template.MetaInfRecommender;
import integrate.recommenders.ironman.generate.sirius.generator.template.RecommendItemExtendedAction;
import integrate.recommenders.ironman.generate.sirius.generator.template.RecommenderCase;
import integrate.recommenders.ironman.generate.sirius.generator.template.RecommenderUtils;
import integrate.recommenders.ironman.generate.sirius.generator.template.dialog.NameColumLabelProvider;
import integrate.recommenders.ironman.generate.sirius.generator.template.dialog.RatingColumLabelProvider;
import integrate.recommenders.ironman.generate.sirius.generator.template.dialog.RecColumLabelProvider;
import integrate.recommenders.ironman.generate.sirius.generator.template.dialog.RecommenderData;
import integrate.recommenders.ironman.generate.sirius.generator.template.dialog.RecommenderDialog;
import project.generator.api.template.sirius.ViewpointEditorPluginXML;
import project.generator.api.utils.WriteUtils;

import static integrate.recommenders.ironman.generate.sirius.dialog.utils.DesignGeneratorUtils.*;
import static project.generator.api.vp.utils.ViewpointSpecificationProjectExtended.*;

public class CreateRecommenderArtifacts {
	
	private final String projectName;
	private final EList<DiagramDescription> selectedDiagramDesc;
	private final Map<String,List<Service>> recommenderToServices;
	private final MLMappingConfiguration mapping;
	private final IStrategyGenerateMenu generateMenu;
	private final String dataFusionAlgorithm;
	
	public CreateRecommenderArtifacts(final String projectName, final EList<DiagramDescription> selectedDiagramDesc, 
			final Map<String,List<Service>> recommenderToServices, MLMappingConfiguration mapping, String dataFusionAlgorithm) {
		this.projectName = projectName;
		this.selectedDiagramDesc = selectedDiagramDesc;		
		this.recommenderToServices = recommenderToServices;
		this.dataFusionAlgorithm = dataFusionAlgorithm;
		this.mapping = mapping;
		this.generateMenu = new GenerateMenuArtifacts();	
	}
	
	public void doGenerateViewpointSpecificationProject() {
		//Create ResourceSet
		final ResourceSet reset = new ResourceSetImpl();
		//1. Load resources
		final Group groupBaseRecommender = copyOfBaseRecommenderGroup(reset);
		//2. Refine viewpoint
		this.generateMenu.generateMenuArtifacts(this.projectName, groupBaseRecommender, selectedDiagramDesc, recommenderToServices, mapping);
		//3. Create Viewpoint Specification Project
		final IProject viewpointProject = createViewpointProject(VIEWPOINT_RECOMMENDER_NAME
				+ "." + VIEWPOINT_MODEL_EXTENSION, groupBaseRecommender);		
		//4. Create all files
		generateAll(viewpointProject);
	}	
	

	private IProject createViewpointProject(final String viewpointName, final Group groupBaseRecommender) {
		try {
			return createNewViewpointSpecificationProject(this.projectName, viewpointName, groupBaseRecommender, false);
		} catch (CoreException e) {			
			e.printStackTrace();
		}
		return null;
	}

	public void generateAll(IProject viewpointProject){
		final List<Runnable> allFiles = init(viewpointProject);
		execute(allFiles);
	}

	private List<Runnable> init(IProject viewpointProject) {
		var allFiles = new ArrayList<Runnable>();
		final String packageName = viewpointProject.getName() + PACKAGE_ACTIONS;
		final String packageNameUtils = viewpointProject.getName() + PACKAGE_UTILS;
		final String packageNameDialog = viewpointProject.getName() + PACKAGE_DIALOG;
		final Set<String> setOfItems = getAllStringsItemsMapping();
		final Map<Item,List<Service>> itemToService = getAllItemsMapping();
		
		generateInfrastructureClasses(viewpointProject, allFiles, packageName, packageNameUtils, packageNameDialog, setOfItems);
		
		generateDialogClasses(viewpointProject, packageNameDialog, packageNameUtils, allFiles);
		
		//Actions package
		for (Map.Entry<Item, List<Service>> entry : itemToService.entrySet()) {
			final Item item = entry.getKey();
			final List<Service> service = entry.getValue();
			final String className = "Recommend" + getClassSuffix(item);
			allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
					+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/actions/"), 
						className + ".java", 
					new RecommendItemExtendedAction(className, packageName, packageNameUtils, 
							packageNameDialog, item, this.recommenderToServices, this.dataFusionAlgorithm,
							this.mapping, service).doGenerate()));
		}		
		return allFiles;
	}
	
	private Set<String> getAllStringsItemsMapping() {
		if (this.mapping == null) {
			return getAllStringItems(this.recommenderToServices);
		} else {
			return this.mapping.getMapTargetElementToTargetItems()
						.values().stream()
						.flatMap(listOfItems -> listOfItems.stream())
						.map(item -> item.getRead().getStructFeature().getName())
						.collect(Collectors.toSet())
						;				
		}
	}
	
	
	private Map<Item,List<Service>> getAllItemsMapping() {
		final Map<Item,List<Service>> itemToService = getAllItems(this.recommenderToServices);
		if(mapping != null) {
			itemToService.entrySet().removeIf(entry -> !itemIsPresent(entry.getKey()));
		} 
		return itemToService;
	}

	private boolean itemIsPresent(Item key) {
		return this.mapping.getMapTargetElementToTargetItems().values()
			.stream().flatMap(listOfItems -> listOfItems.stream())
			.filter(item -> item.getRead().getItem().equals(key.getRead())).findAny().isPresent();		
	}
	
	private String getClassSuffix(Item item) {
		if (this.mapping == null) {
			return item.getRead();
		} else {
			return mappingitem(item).getRead().getStructFeature().getName();
		}
	}
	
	private TargetItemElement mappingitem(Item key) {
		return this.mapping.getMapTargetElementToTargetItems().values()
			.stream().flatMap(listOfItems -> listOfItems.stream())
			.filter(item -> item.getRead().getItem().equals(key.getRead())).findAny().get();		
	}

	private void generateDialogClasses(IProject viewpointProject, String packageNameDialog, String packageNameUtils, ArrayList<Runnable> allFiles) {
		//Generate RecommenderUtils
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/dialog/"), 
				"RecommenderDialog" + ".java", 
			new RecommenderDialog(packageNameDialog, packageNameUtils).doGenerate()));	
		
		//Generate RecommenderData
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/dialog/"), 
				"RecommenderData" + ".java", 
			new RecommenderData(packageNameDialog).doGenerate()));
		
		//Generate NameColumLabelProvider
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/dialog/"), 
				"NameColumLabelProvider" + ".java", 
			new NameColumLabelProvider(packageNameDialog).doGenerate()));
		
		//Generate NameColumLabelProvider
				allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
						+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/dialog/"), 
						"RecColumLabelProvider" + ".java", 
					new RecColumLabelProvider(packageNameDialog).doGenerate()));
		//Generate NameColumLabelProvider
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/dialog/"), 
				"RatingColumLabelProvider" + ".java", 
			new RatingColumLabelProvider(packageNameDialog).doGenerate()));
	}

	private void generateInfrastructureClasses(IProject viewpointProject, ArrayList<Runnable> allFiles,
			final String packageName, final String packageNameUtils, String packageNameDialog, final Set<String> setOfItems) {
		//Generate Plugin.XML
		allFiles.add(() -> WriteUtils.write(viewpointProject, "plugin.xml", 
				new ViewpointEditorPluginXML(viewpointProject,packageName,
						setOfItems).doGenerate()));
		//Generate MANIFEST.MF
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/META-INF"), "MANIFEST.MF", 
				new MetaInfRecommender(viewpointProject, this.recommenderToServices, mapping).doGenerate()));
		//Generate RecommenderCase
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/utils/"), 
				"RecommenderCase" + ".java", 
			new RecommenderCase(packageNameUtils).doGenerate()));
		//Generate RecommenderUtils
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
				+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/utils/"), 
				"RecommenderUtils" + ".java", 
			new RecommenderUtils(packageNameUtils, packageNameDialog, this.recommenderToServices).doGenerate()));
	}	
	
	private void execute(List<Runnable> allFiles){
		allFiles.forEach(Runnable::run);
	}	
}
