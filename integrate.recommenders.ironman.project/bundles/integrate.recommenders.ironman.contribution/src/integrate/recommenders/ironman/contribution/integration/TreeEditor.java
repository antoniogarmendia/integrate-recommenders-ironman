package integrate.recommenders.ironman.contribution.integration;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import project.generator.api.CreateProjectEngine;
import project.generator.api.ProjectFeatures;
import integrate.recommenders.ironman.definition.integration.IIntegration;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.tree.editor.GenerateTreeEditorProject;
import integrate.recommenders.ironman.generate.tree.editor.dialog.TreeEditorDialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

public class TreeEditor implements IIntegration {

	public static final String TREE_EDITOR_INTEGRATION_SUFFIX = "tree.integration";
	private String projectName;
	private MLMappingConfiguration mapping;
	
	public TreeEditor() {
		this.projectName = TREE_EDITOR_INTEGRATION_SUFFIX;
	}

	@Override
	public boolean configure(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		this.mapping = mapping;
		return true;
	}

	@Override
	public boolean cutomizeIntegration() {
		final TreeEditorDialog treeEditorDialog = new TreeEditorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				projectName);
		if (treeEditorDialog.open() == Window.OK) {
			this.projectName = treeEditorDialog.getProjectName();
		}		
		return true;
	}

	@Override
	public void generateIntegration(String dataFusionAlgorithm, Map<String, List<Service>> recommenderToServices) {
		// Create Project
		final ProjectFeatures projectFeat = new ProjectFeatures.Builder(this.projectName)
					.isJavaProject(true)
					.javaSE("JavaSE-17")
					.isPlugin(true)
					.build();
		final IProject project = new CreateProjectEngine(projectFeat).doGenerateProject(new NullProgressMonitor(), true);		
		new GenerateTreeEditorProject(project,recommenderToServices,mapping,dataFusionAlgorithm).generateAll();	
	}

}
