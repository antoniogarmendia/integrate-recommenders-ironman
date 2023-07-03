package integrate.recommenders.ironman.contribution.integration;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.ui.PlatformUI;

import integrate.recommenders.ironman.definition.integration.IIntegration;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.dialog.SiriusViewpointDialog;
import integrate.recommenders.ironman.generate.sirius.generator.CreateRecommenderArtifacts;

public class SiriusIntegration implements IIntegration {
	
	private Map<String,List<Service>> recommenderToServices; 
	private MLMappingConfiguration mapping;
	private String projectName;
	private EList<DiagramDescription> selectedDiagramDesc;

	public SiriusIntegration() {
		// Do nothing
	}

	@Override
	public boolean configure(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		this.recommenderToServices = recommenderToServices;
		this.mapping = mapping;
		return true;
	}

	@Override
	public boolean cutomizeIntegration() {
		final String nsURI = this.getMetamodelFromRecommenderToServices();
		final SiriusViewpointDialog siriusDialog = new SiriusViewpointDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				nsURI);
		siriusDialog.open();
		if (siriusDialog.getSelectedDiagramDescriptions().size() != 0) {
			this.selectedDiagramDesc = siriusDialog.getSelectedDiagramDescriptions();
			this.projectName = siriusDialog.getProjectName();
			return true;
		}
		
		return false;
	}

	//Get the nsURI of the meta-model. Get any nsURI because all the nsURI are the same.
	private String getMetamodelFromRecommenderToServices() {
		//First Check Mapping
		if (this.mapping != null)
			return this.mapping.getEPackage().getNsURI();
		return this.recommenderToServices.entrySet().stream().findAny().get().getValue().get(0).getDetail().getNsURI();
	}

	@Override
	public void generateIntegration(String dataFusionAlgorithm, Map<String, List<Service>> recommenderToServices) {
		new CreateRecommenderArtifacts(projectName, 
				selectedDiagramDesc,
				this.recommenderToServices,
				this.mapping, dataFusionAlgorithm).doGenerateViewpointSpecificationProject();	
		System.out.println("Create Viewpoint Specification Project");
	}

}
