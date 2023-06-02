package integrate.recommenders.ironman.definition.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

import integrate.recommenders.ironman.definition.services.Service;

public class EvaluateContributionsHandler {
	
	private static final String IRONMAN_IINTEGRATION_ID = "integrate.recommender.environment";
	
	public static Map<String, IIntegration> getPlatformIntegrationExtensions(){
		return getAllIntegrationExtensions(Platform.getExtensionRegistry());
	} 	
	
	public static Map<String, IIntegration> getAllIntegrationExtensions(IExtensionRegistry registry){
		Map<String, IIntegration> extensionToIIntegration = new HashMap<>();
		IConfigurationElement[] extensions =
                registry.getConfigurationElementsFor(IRONMAN_IINTEGRATION_ID);
		for (IConfigurationElement config : extensions) {
			String name = config.getAttribute("name");
			try {
				final Object o =
						config.createExecutableExtension("class");
				if (o instanceof IIntegration)
					extensionToIIntegration.put(name, (IIntegration) o);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}		
		return extensionToIIntegration;
	}
	
	public static boolean executeConfigure(final IIntegration integration, final Map<String, List<Service>> recommenderToServices) {
		ExecuteConfigureIntegration execute = new ExecuteConfigureIntegration(integration, recommenderToServices);
		SafeRunner.run(execute);		
		return execute.get();					
    }
	
	public static boolean executeCutomizeIntegration(final IIntegration integration, final Map<String, List<Service>> recommenderToServices) {
		ExecuteCustomizeIntegration execute = new ExecuteCustomizeIntegration(integration, recommenderToServices);	
		SafeRunner.run(execute);
		return execute.get();
    }
	
	public static void executeGenerateIntegration(
			final String metasearchAlgorithm, final IIntegration integration,
			final Map<String, List<Service>> recommenderToServices) {
		ISafeRunnable runnable = new ISafeRunnable() {
            @Override
            public void handleException(Throwable e) {
                System.out.println("Exception in client");
            }

            @Override
            public void run() throws Exception {
               integration.generateIntegration(metasearchAlgorithm, recommenderToServices);
            }
        };
        SafeRunner.run(runnable);		
    }
}