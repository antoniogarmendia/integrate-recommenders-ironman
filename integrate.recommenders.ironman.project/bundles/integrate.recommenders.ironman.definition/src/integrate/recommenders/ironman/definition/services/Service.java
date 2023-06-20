package integrate.recommenders.ironman.definition.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;

//TODO add in another class?
//@JsonIgnoreProperties(value = { "ePackage" })
//@JsonDeserialize(using = ServiceDeserializer.class)
public class Service {	
	
	private String name;
	private List<Detail> details;
	
	public String getName() {
		return name;
	}
	
	public List<Detail> getDetails() {
		return details;
	}
	
	public Detail getDetail() {
		return getDetails().get(0);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDetails(List<Detail> details) {
		this.details = details;
	}
	
	//TODO getEPackage() use nsURI
	//private String nsURI;
	//private EPackage ePackage = null;
//	public void setNsURI(String nsURI) {
//		this.nsURI = nsURI;
//	}
//	
//	public String getNsURI() {
//		return nsURI;
//	}

	//TODO add in another class?
//	public EPackage getEPackage() {
//		if (ePackage == null) {
//			this.ePackage = EPackageRegistryImpl.INSTANCE.getEPackage(this.nsURI);
//		}
//		return this.ePackage;
//	}	
}
