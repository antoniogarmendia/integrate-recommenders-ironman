package integrate.recommenders.ironman.generate.tree.editor;

import integrate.recommenders.ironman.generate.tree.editor.template.MetaInfRecommender;
import integrate.recommenders.ironman.definition.services.Service;
import project.generator.api.utils.WriteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;

public class GenerateTreeEditorProject {
	
	private final Map<String, List<Service>> recommenderToServices;
	private final IProject project;
	
	public GenerateTreeEditorProject(IProject project, Map<String, List<Service>> recommenderToServices) {
		this.project = project;
		this.recommenderToServices = recommenderToServices;
	}

	public void generateAll() {
		final List<Runnable> allFiles = init();
		execute(allFiles);		
	}
	
	public List<Runnable> init() {
		var allFiles = new ArrayList<Runnable>();
		//TODO Fix
//		allFiles.add(() -> WriteUtils.write(this.project, "plugin.xml", new TreeEditorPluginXML(recommenderCofig).doGenerate()));
//		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/src/recommender/popup/menu"), 
//				"RecommenderPopup.java", new RecommenderPopup(recommenderCofig).doGenerate()));	
		allFiles.add(() -> WriteUtils.write(this.project.getFolder("/META-INF"), 
				"MANIFEST.MF", new MetaInfRecommender(this.project).doGenerate()));
		
		return allFiles;
	}
	
	private void execute(List<Runnable> allFiles){
		allFiles.forEach(Runnable::run);;
	}

}
