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
import integrate.recommenders.ironman.generate.sirius.generator.template.MetaInfRecommender;
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
		if (this.mapping == null) {
			this.generateMenu = new GenerateMenuArtifacts();
		} else {
			this.generateMenu = new GenerateMenuArtifactsMapping();
		}
	}
	
	public void doGenerateViewpointSpecificationProject() {
		//Create ResourceSet
		final ResourceSet reset = new ResourceSetImpl();
		//1. Load resources
		final Group groupBaseRecommender = copyOfBaseRecommenderGroup(reset);
		//2. Refine viewpoint
		this.generateMenu.generateMenuArtifacts(this.projectName, groupBaseRecommender, selectedDiagramDesc, recommenderToServices);
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
		final Set<String> setOfItems = getAllItems(this.recommenderToServices);
		//Generate Plugin.XML
		allFiles.add(() -> WriteUtils.write(viewpointProject, "plugin.xml", 
				new ViewpointEditorPluginXML(viewpointProject,packageName,
						setOfItems).doGenerate()));
		//Generate MANIFEST.MF
		allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/META-INF"), "MANIFEST.MF", 
				new MetaInfRecommender(viewpointProject, this.recommenderToServices).doGenerate()));
		
		//Actions package
		for (String item : setOfItems) {
			final String className = "Recommend" + item;
			allFiles.add(() -> WriteUtils.write(viewpointProject.getFolder("/src/" 
					+  viewpointProject.getName().replaceAll(DOT_SEPARATOR_PATH, "/") + "/actions/"), 
						className + ".java", 
					new RecommendItemExtendedAction(className, packageName, item, this.recommenderToServices).doGenerate()));
		}
		
		return allFiles;
	}	
	
	private void execute(List<Runnable> allFiles){
		allFiles.forEach(Runnable::run);
	}	
}
