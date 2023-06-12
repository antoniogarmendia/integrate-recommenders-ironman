package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;

public class MapItemElement {
	
	private String item;
	private EStructuralFeature writeElement;
	
	public MapItemElement() {
		this.item = null;
		this.writeElement = null;
	}
	
	public MapItemElement(String item, EStructuralFeature writeElement) {
		this.item = item;
		this.writeElement = writeElement;
	}
	
	public ENamedElement getWriteElement() {
		return writeElement;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setWriteElement(EStructuralFeature writeElement) {
		this.writeElement = writeElement;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
}
