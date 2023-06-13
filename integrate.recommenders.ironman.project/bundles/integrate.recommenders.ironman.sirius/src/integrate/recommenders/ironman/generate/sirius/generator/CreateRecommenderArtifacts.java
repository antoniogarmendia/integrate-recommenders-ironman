package integrate.recommenders.ironman.generate.sirius.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramExtensionDescription;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.tool.ExternalJavaAction;
import org.eclipse.sirius.viewpoint.description.tool.PopupMenu;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.api.IStrategyGenerateMenu;
import integrate.recommenders.ironman.generate.sirius.generator.template.RecommendItemExtendedAction;
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
	
	public CreateRecommenderArtifacts(final String projectName, final EList<DiagramDescription> selectedDiagramDesc, 
			final Map<String,List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		this.projectName = projectName;
		this.selectedDiagramDesc = selectedDiagramDesc;		
		this.recommenderToServices = recommenderToServices;
		this.mapping = mapping;
		if (this.mapping.getEPackage() != null) {
			this.generateMenu = new GenerateMenuArtifactsMapping();
		} else {
			this.generateMenu = new GenerateMenuArtifacts();
		}
	}
	
	public void doGenerateViewpointSpecificationProject() {
		//Create ResourceSet
		final ResourceSet reset = new ResourceSetImpl();
		//1. Load resources
		final Group groupBaseRecommender = copyOfBaseRecommenderGroup(reset);
		//2. Refine viewpoint
		refineViewpoint(groupBaseRecommender);
		//3. Create Viewpoint Specification Project
		final IProject viewpointProject = createViepointProject(VIEWPOINT_RECOMMENDER_NAME
				+ "." + VIEWPOINT_MODEL_EXTENSION, groupBaseRecommender);		
		//4. Create all files
		generateAll(viewpointProject);
	}
	
	private void refineViewpoint(final Group groupBaseRecommender) {
		// Get source diagram extension
		final Viewpoint recommenderVp = groupBaseRecommender.getOwnedViewpoints().get(0);
		final DiagramExtensionDescription diagramDesc = (DiagramExtensionDescription) recommenderVp.getOwnedRepresentationExtensions().get(0);
		for (DiagramDescription diagramDescription : selectedDiagramDesc) {
			final DiagramExtensionDescription currentDiagDescrip = EcoreUtil.copy(diagramDesc);
			currentDiagDescrip.setName("Recommender-" + diagramDescription.getName());
			currentDiagDescrip.setViewpointURI(getViewpointURIFromDiagramDescription(diagramDescription));
			currentDiagDescrip.setRepresentationName(diagramDescription.getName());
			recommenderVp.getOwnedRepresentationExtensions().add(currentDiagDescrip);
			//Add menu to each layer
			addMenuForItems(currentDiagDescrip);
		}
		EcoreUtil.remove(diagramDesc);		
	}

	private IProject createViepointProject(final String viewpointName, final Group groupBaseRecommender) {
		try {
			return createNewViewpointSpecificationProject(this.projectName, viewpointName, groupBaseRecommender);
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
		final Set<String> setOfItems = getAllItems(this.recommenderToServices);
		allFiles.add(() -> WriteUtils.write(viewpointProject, "plugin.xml", 
				new ViewpointEditorPluginXML(viewpointProject,packageName,
						setOfItems).doGenerate()));
		//Actions package
		for (String item : setOfItems) {
			final String className = "Recommend" + item;
			allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
					+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/actions/"), 
						className + ".java", 
					new RecommendItemExtendedAction(className, packageName, this.recommenderToServices).doGenerate()));
		}
		
		return allFiles;
	}	
	
	private EPackage getEPackageByNsURI() {
		final String nsURI = recommenderToServices.entrySet().stream().findAny().orElseThrow().getValue().get(0).getNsURI();
		return EPackageRegistryImpl.INSTANCE.getEPackage(nsURI);		
	}
	
	private String getTarget() {
		final String target = recommenderToServices.entrySet()
									.stream().findAny().orElseThrow()
									.getValue().get(0).getServices().get(0).getDetails().getTarget();
		return target;		
	}

	private void execute(List<Runnable> allFiles){
		allFiles.forEach(Runnable::run);
	}
	
	private void addMenuForItems(DiagramExtensionDescription currentDiagDescrip) {
		final String packageName = projectName + PACKAGE_ACTIONS;
		//Get PopupMenu
		final PopupMenu popupMenu = currentDiagDescrip.getLayers().get(0).getToolSections().get(0).getPopupMenus().get(0);
		popupMenu.setLabel("Recommender-" + getTarget());
		final Set<String> setOfItems = getAllItems(this.recommenderToServices);
		//Add menu for each item
		for (String item : setOfItems) {
			ExternalJavaAction externalJavaAction = ToolFactory.eINSTANCE.createExternalJavaAction();
			externalJavaAction.setId(packageName + "." + item);
			externalJavaAction.setName(packageName + ".Recommend" + item);
			externalJavaAction.setLabel(getETypefromItem(item));
			popupMenu.getMenuItemDescription().add(externalJavaAction);
		}
	}
	
	private String getETypefromItem(String item) {
		final EClassifier classifier = getEPackageByNsURI().getEClassifier(getTarget());
		if (classifier instanceof EClass) {
			final EClass eClass = (EClass) classifier; 
			final EStructuralFeature feature = eClass.getEAllStructuralFeatures()
											.stream()
											.filter(struct -> struct.getName().equalsIgnoreCase(item))
											.findAny().orElseThrow();
			return feature.getEType().getName();
		}
		return null;
	}
}
