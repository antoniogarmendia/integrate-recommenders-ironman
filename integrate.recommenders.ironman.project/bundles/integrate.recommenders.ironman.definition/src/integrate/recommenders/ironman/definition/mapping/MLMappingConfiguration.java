package integrate.recommenders.ironman.definition.mapping;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EPackage;

public class MLMappingConfiguration {
	
	//Target and Items
	private Map<TargetElement, List<TargetItemElement>> mapTargetElementToTargetItems;
	
	//Package
	private String nsURIPackage;
	
	//GenModel that represents the language
	private GenModel genModel;	
	
	public MLMappingConfiguration() {
		// Do nothing
	}
	
	public MLMappingConfiguration(Map<TargetElement,List<TargetItemElement>> mapTargetElementToTargetItems,
			GenModel genModel, String nsURIPackage) {
		this.mapTargetElementToTargetItems = mapTargetElementToTargetItems;
		this.genModel = genModel;
		this.nsURIPackage = nsURIPackage;
	}
	
	public void setMapTargetElementToTargetItems(
			Map<TargetElement, List<TargetItemElement>> mapTargetElementToTargetItems) {
		this.mapTargetElementToTargetItems = mapTargetElementToTargetItems;
	}
	
	public Map<TargetElement, List<TargetItemElement>> getMapTargetElementToTargetItems() {
		return mapTargetElementToTargetItems;
	}
	
	public EPackage getEPackage() {
		if (this.genModel != null) {
			if (this.genModel.getGenPackages().get(0).getEcorePackage().getNsURI().equals(this.nsURIPackage)) 
				return this.genModel.getGenPackages().get(0).getEcorePackage();
			else {
				var genNestedPackage = this.genModel.getGenPackages().get(0).getNestedGenPackages().stream()
							.filter(genPackage -> genPackage.getEcorePackage().getNsURI().equals(this.nsURIPackage))
							.findAny()
							.orElse(this.genModel.getGenPackages().get(0))
							;	
				return genNestedPackage.getEcorePackage();
			}
		}
		else 
			return null;
	}	
	
	public GenModel getGenModel() {
		return genModel;
	}
	
	public String getNsURIPackage() {
		return nsURIPackage;
	}
	
	public void setGenModel(GenModel genModel) {
		this.genModel = genModel;
	}	
	
	public void setNsURIPackage(String nsURIPackage) {
		this.nsURIPackage = nsURIPackage;
	}
}
