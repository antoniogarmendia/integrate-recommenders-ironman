package integrate.recommenders.ironman.contribution.integration;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.integration.IIntegration;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;

public class SiriusIntegration implements IIntegration {
	
	private Map<String,List<Service>> recommenderToServices; 

	public SiriusIntegration() {
		// Do nothing
	}

	@Override
	public boolean configure(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		this.recommenderToServices = recommenderToServices;
		return true;
	}

	@Override
	public boolean cutomizeIntegration() {
		final SiriusViewpointDialog siriusDialog = new SiriusViewpointDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				this.getMetamodelFromRecommenderToServices());
		
		
		return false;
	}

	@Override
	public void generateIntegration(String dataFusionAlgorithm, Map<String, List<Service>> recommenderToServices) {
		// TODO Auto-generated method stub
		System.out.println("Inspect");
	}

}
