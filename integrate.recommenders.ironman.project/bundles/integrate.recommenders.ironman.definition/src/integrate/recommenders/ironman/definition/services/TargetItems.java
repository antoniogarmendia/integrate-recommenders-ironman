package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Deprecated
@JsonIgnoreProperties(value = { "targetEClass" , "itemsEStrucFeat"})
public class TargetItems {
	
	private Service service;
	private String target;
	private List<String> items = new ArrayList<String>();
	private EClass targetEClass = null;
	private EList<EStructuralFeature> itemsEStrucFeat = new BasicEList<EStructuralFeature>();
	
	public TargetItems() { 	}
	
	public void setItems(List<String> items) {
		this.items = items;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	public Service getService() {
		return service;
	}
	
	public String getTarget() {
		return target;
	}
	
	public List<String> getItems() {
		return items;
	}
	
//	public EClass getTargetEClass() {
//		if(this.targetEClass == null) {
//			this.targetEClass = getService().getEPackage()
//									.getEClassifiers().stream()
//									.filter(eClassifier -> eClassifier.getName().equals(this.target))
//									.findAny()
//									.map(EClass.class::cast)
//									.orElseThrow()
//									;	
//		}			
//		return this.targetEClass;	
//	}
//	
//	public EList<EStructuralFeature> getItemsEStrucFeat() {
//		if (this.itemsEStrucFeat.isEmpty()) {
//			this.itemsEStrucFeat = getTargetEClass().getEAllStructuralFeatures()
//										.stream()
//										.filter(structFeat -> getItems().contains(structFeat.getName()))
//										.collect(Collectors.toCollection(BasicEList::new));
//		}
//		return itemsEStrucFeat;
//	}
}
