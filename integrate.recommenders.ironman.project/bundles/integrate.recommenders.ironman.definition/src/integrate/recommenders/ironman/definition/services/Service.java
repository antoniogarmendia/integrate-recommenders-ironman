package integrate.recommenders.ironman.definition.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;

@JsonIgnoreProperties(value = { "ePackage" })
@JsonDeserialize(using = ServiceDeserializer.class)
public class Service {
	
	private String nsURI;
	private List<Recommender> services = new ArrayList<Recommender>();
	private EPackage ePackage = null;
	
	public List<Recommender> getServices() {
		return services;
	}
	
	public void setServices(List<Recommender> services) {
		this.services = services;
	}
	
	public void setNsURI(String nsURI) {
		this.nsURI = nsURI;
	}
	
	public String getNsURI() {
		return nsURI;
	}
	
	public EPackage getEPackage() {
		if (ePackage == null) {
			this.ePackage = EPackageRegistryImpl.INSTANCE.getEPackage(this.nsURI);
		}
		return this.ePackage;
	}	
}
