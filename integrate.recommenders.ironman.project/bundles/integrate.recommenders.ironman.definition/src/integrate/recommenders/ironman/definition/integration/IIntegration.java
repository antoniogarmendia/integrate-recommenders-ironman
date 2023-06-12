package integrate.recommenders.ironman.definition.integration;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;

public interface IIntegration {
	
	/**
	 *  This method should return false if the Integration no need extra configuration
	 * @param recommenderCofig
	 * @return
	 */
	public boolean configure(final Map<String,List<Service>> recommenderToServices,
								final MLMappingConfiguration mapping);
	
	/**
	 * This method is called if configure returns true
	 * If returns true, it means that the user configure all the features on the client-side
	 */
	public boolean cutomizeIntegration();
	
	/**
	 * This method will be called if the user select the integration
	 * @param dataFusionAlgorithm the id of the extension that implements the dataFusionAlgorithm
	 */
	public void generateIntegration(final String dataFusionAlgorithm, 
			final Map<String,List<Service>> recommenderToServices);

}
