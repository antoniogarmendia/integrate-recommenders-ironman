package integrate.recommenders.ironman.definition.integration;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ISafeRunnable;

import integrate.recommenders.ironman.definition.services.Service;

public abstract class SafeRunnableType<T> implements ISafeRunnable {
	
	protected final IIntegration integration;
	protected final Map<String, List<Service>> recommenderToServices;
	protected T t;
	
	public SafeRunnableType(IIntegration integration, Map<String, List<Service>> recommenderToServices) {
		this.integration = integration;		
		this.recommenderToServices = recommenderToServices;
	}
	
	@Override
	public void handleException(Throwable exception) {
		System.out.println("Exception in client");
	}
	
	public T get(){
		return this.t;
	}	
	
}
