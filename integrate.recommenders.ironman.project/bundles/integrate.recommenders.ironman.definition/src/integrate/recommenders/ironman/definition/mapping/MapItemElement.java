package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;

public class MapItemElement {
	
	private String item;
	private EClass typeEClass;
	private EStructuralFeature feature;
	private EStructuralFeature writeElement;
	
	public MapItemElement() {
		this.item = null;
		this.writeElement = null;
	}
	
	public MapItemElement(String item, EStructuralFeature writeElement) {
		this.item = item;
		this.writeElement = writeElement;
	}
	
	public EClass getTypeEClass() {
		return typeEClass;
	}
	
	public EStructuralFeature getFeature() {
		return feature;
	}
	
	public ENamedElement getWriteElement() {
		return writeElement;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setFeature(EStructuralFeature feature) {
		this.feature = feature;
	}
	
	public void setTypeEClass(EClass typeEClass) {
		this.typeEClass = typeEClass;
	}
	
	public void setWriteElement(EStructuralFeature writeElement) {
		this.writeElement = writeElement;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
}
