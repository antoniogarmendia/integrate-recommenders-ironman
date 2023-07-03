package integrate.recommenders.ironman.definition.mapping;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EPackage;

public class MLMappingConfiguration {
	
	//Target and Items
	private Map<TargetElement, List<TargetItemElement>> mapTargetElementToTargetItems;
		
	//GenModel that represents the language
	private GenModel genModel;	
	
	public MLMappingConfiguration() {
		// Do nothing
	}
	
	public MLMappingConfiguration(Map<TargetElement,List<TargetItemElement>> mapTargetElementToTargetItems,
			GenModel genModel) {
		this.mapTargetElementToTargetItems = mapTargetElementToTargetItems;
		this.genModel = genModel;
	}
	
	public void setMapTargetElementToTargetItems(
			Map<TargetElement, List<TargetItemElement>> mapTargetElementToTargetItems) {
		this.mapTargetElementToTargetItems = mapTargetElementToTargetItems;
	}
	
	public Map<TargetElement, List<TargetItemElement>> getMapTargetElementToTargetItems() {
		return mapTargetElementToTargetItems;
	}
	
	public EPackage getEPackage() {
		if (this.genModel != null)
			return this.genModel.getGenPackages().get(0).getEcorePackage();
		else 
			return null;
	}	
	
	public GenModel getGenModel() {
		return genModel;
	}
	
	public void setGenModel(GenModel genModel) {
		this.genModel = genModel;
	}	
}
