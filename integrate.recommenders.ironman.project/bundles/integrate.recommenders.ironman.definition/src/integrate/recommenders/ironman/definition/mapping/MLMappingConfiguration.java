package integrate.recommenders.ironman.definition.mapping;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EPackage;

public class MLMappingConfiguration {
	
	//Target and Items
	//Key = target Element
	private Map<MapTargetElement, List<MapItemElement>> sourceToTargetMap;
	
	//GenModel that represents the language
	private GenModel genModel;
	
	public MLMappingConfiguration(Map<MapTargetElement,List<MapItemElement>> sourceToTargetMap, GenModel genModel) {
		this.sourceToTargetMap = sourceToTargetMap;
		this.genModel = genModel;
	}
	
	public EPackage getEPackage() {
		if (this.genModel != null)
			return this.genModel.getGenPackages().get(0).getEcorePackage();
		else 
			return null;
	}
	
	public Map<MapTargetElement, List<MapItemElement>> getSourceToTargetMap() {
		return sourceToTargetMap;
	}
	
	public void setGenModel(GenModel genModel) {
		this.genModel = genModel;
	}	
	
	public void setSourceToTargetMap(Map<MapTargetElement, List<MapItemElement>> sourceToTargetMap) {
		this.sourceToTargetMap = sourceToTargetMap;
	}
}
