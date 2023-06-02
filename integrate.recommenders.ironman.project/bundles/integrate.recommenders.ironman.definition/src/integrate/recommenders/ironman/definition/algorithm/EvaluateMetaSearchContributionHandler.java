package integrate.recommenders.ironman.definition.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;

public class EvaluateMetaSearchContributionHandler {
	
	private static final String IRONMAN_META_SEARCH_ID = "integrate.recommender.metasearch.algorithm";
	
	public static Collection<String> getPlatformIntegrationExtensions(){
		return getAllMetaSearchAlgorithmExtensions(Platform.getExtensionRegistry());
	}

	private static Collection<String> getAllMetaSearchAlgorithmExtensions(IExtensionRegistry registry) {
		List<String> metaSearchExtensions = new ArrayList<String>();
		IConfigurationElement[] extensions =
                registry.getConfigurationElementsFor(IRONMAN_META_SEARCH_ID);
		for (IConfigurationElement config : extensions) {
			metaSearchExtensions.add(config.getAttribute("name"));			
		}		
		return metaSearchExtensions;
	}
	
	public static Map<String, Double> executeMetaSearchAlgorithByName(String extensionName, 
			Map<String, List<ItemRecommender>> recommendations) {
		IConfigurationElement[] extensions =
				Platform.getExtensionRegistry().getConfigurationElementsFor(IRONMAN_META_SEARCH_ID);
		for (IConfigurationElement config : extensions) {
			if (config.getAttribute("name").equals(extensionName)) {
				try {
					final Object o = config.createExecutableExtension("class");
					if (o instanceof IMetaSearchAlgorithm) {
						return executeExtension((IMetaSearchAlgorithm)o, recommendations);
					}					
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}		
		return null;
	}

	private static Map<String, Double> executeExtension(IMetaSearchAlgorithm metaSearchAlgo, 
			Map<String, List<ItemRecommender>> recommendations) {
		//TODO implement this better SafeRunner?
		return metaSearchAlgo.algorithm(recommendations);
	}

}
