package integrate.recommenders.ironman.generate.tree.editor;

import integrate.recommenders.ironman.generate.tree.editor.template.MetaInfRecommender;
import integrate.recommenders.ironman.generate.tree.editor.template.RecommenderCase;
import integrate.recommenders.ironman.generate.tree.editor.template.RecommenderData;
import integrate.recommenders.ironman.generate.tree.editor.template.TreeEditorPluginXML;
import integrate.recommenders.ironman.generate.tree.editor.template.dialog.NameColumLabelProvider;
import integrate.recommenders.ironman.generate.tree.editor.template.dialog.RatingColumLabelProvider;
import integrate.recommenders.ironman.generate.tree.editor.template.dialog.RecColumLabelProvider;
import integrate.recommenders.ironman.generate.tree.editor.template.dialog.RecommenderDialog;
import integrate.recommenders.ironman.generate.tree.editor.template.RecommenderPopup;
import integrate.recommenders.ironman.generate.tree.editor.template.RecommenderUtils;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;
import project.generator.api.utils.WriteUtils;

import static project.generator.api.vp.utils.ViewpointSpecificationProjectExtended.DOT_SEPARATOR_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;

public class GenerateTreeEditorProject {
	
	private final Map<String, List<Service>> recommenderToServices;
	private final IProject project;
	private MLMappingConfiguration mapping;
	private final String dataFusionAlgorithm;
	
	public GenerateTreeEditorProject(IProject project, Map<String, List<Service>> recommenderToServices, 
			MLMappingConfiguration mapping, String dataFusionAlgorithm) {
		this.project = project;
		this.recommenderToServices = recommenderToServices;
		this.mapping = mapping;
		this.dataFusionAlgorithm = dataFusionAlgorithm;
	}

	public void generateAll() {
		final List<Runnable> allFiles = init();
		execute(allFiles);		
	}
	
	public List<Runnable> init() {
		var allFiles = new ArrayList<Runnable>();
		
		allFiles.add(() -> WriteUtils.write(this.project, "plugin.xml", 
				new TreeEditorPluginXML(this.project.getName()).doGenerate()));
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" +
				this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RecommenderPopup.java", new RecommenderPopup(this.project.getName(),
						recommenderToServices, this.mapping, this.dataFusionAlgorithm).doGenerate()));	
		
		//Generate RecommenderUtils
		allFiles.add(() -> WriteUtils.write(project.getFolder("/src/" 
				+  project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RecommenderUtils" + ".java", 
			new RecommenderUtils(this.project.getName(), this.project.getName(), this.recommenderToServices).doGenerate()));
		
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" +
				this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RecommenderCase.java", new RecommenderCase(this.project.getName()).doGenerate()));	
		
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/META-INF"), 
				"MANIFEST.MF", new MetaInfRecommender(this.project, recommenderToServices).doGenerate()));
		
		generateDialogClasses(this.project, this.project.getName(), this.project.getName(), allFiles);
		
		return allFiles;
	}
	
	private void generateDialogClasses(IProject project, String packageNameDialog, 
			String packageNameUtils, ArrayList<Runnable> allFiles) {
		//Generate RecommenderUtils
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" 
				+  this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RecommenderDialog" + ".java", 
			new RecommenderDialog(packageNameDialog, packageNameUtils).doGenerate()));	
		
		//Generate RecommenderData
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" 
				+  this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RecommenderData" + ".java", 
			new RecommenderData(packageNameDialog).doGenerate()));
		
		//Generate NameColumLabelProvider
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" 
				+  this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"NameColumLabelProvider" + ".java", 
			new NameColumLabelProvider(packageNameDialog).doGenerate()));
		
		//Generate NameColumLabelProvider
				allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" 
						+  this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
						"RecColumLabelProvider" + ".java", 
					new RecColumLabelProvider(packageNameDialog).doGenerate()));
		//Generate NameColumLabelProvider
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/" 
				+  this.project.getName().replaceAll(DOT_SEPARATOR_PATH, "/")), 
				"RatingColumLabelProvider" + ".java", 
			new RatingColumLabelProvider(packageNameDialog).doGenerate()));
	}
	
	private void execute(List<Runnable> allFiles){
		allFiles.forEach(Runnable::run);;
	}

}
