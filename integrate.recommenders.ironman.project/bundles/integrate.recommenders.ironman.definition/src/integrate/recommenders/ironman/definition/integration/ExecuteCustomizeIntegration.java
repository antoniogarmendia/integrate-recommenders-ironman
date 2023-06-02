package integrate.recommenders.ironman.definition.integration;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.services.Service;

public class ExecuteCustomizeIntegration  extends SafeRunnableType<Boolean>{

	public ExecuteCustomizeIntegration(IIntegration integration, Map<String, List<Service>> recommenderToServices) {
		super(integration, recommenderToServices);		
	}

	@Override
	public void run() throws Exception {
		t = integration.cutomizeIntegration();		
	}

}
