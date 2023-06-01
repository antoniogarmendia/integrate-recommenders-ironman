package integrate.recommenders.ironman.contribution.integration;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.integration.IIntegration;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;

public class TreeEditor implements IIntegration {

	public TreeEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean configure(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cutomizeIntegration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generateIntegration(String dataFusionAlgorithm, Map<String, List<Service>> recommenderToServices) {
		// TODO Auto-generated method stub

	}

}
