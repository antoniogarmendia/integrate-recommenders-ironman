package integrate.recommenders.ironman.definition.integration;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ISafeRunnable;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;

public abstract class SafeRunnableType<T> implements ISafeRunnable {
	
	protected final IIntegration integration;
	protected final Map<String, List<Service>> recommenderToServices;
	protected final MLMappingConfiguration mapping;
	protected T t;
	
	public SafeRunnableType(IIntegration integration) {
		this.integration = integration;
		this.recommenderToServices = null;
		this.mapping = null;
	}
	
	public SafeRunnableType(IIntegration integration, Map<String, List<Service>> recommenderToServices, 
			MLMappingConfiguration mapping) {
		this.integration = integration;		
		this.recommenderToServices = recommenderToServices;
		this.mapping = mapping;
	}
	
	@Override
	public void handleException(Throwable exception) {
		System.out.println("Exception in client");
	}
	
	public T get(){
		return this.t;
	}	
	
}
