package integrate.recommenders.ironman.definition.integration;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.services.Service;

public class ExecuteConfigureIntegration extends SafeRunnableType<Boolean> {

	public ExecuteConfigureIntegration(IIntegration integration, Map<String, List<Service>> recommenderToServices) {
		super(integration, recommenderToServices);		
	}

	@Override
	public void run() throws Exception {
		//TODO second argument!
		t = integration.configure(recommenderToServices, null);		
	}

}
