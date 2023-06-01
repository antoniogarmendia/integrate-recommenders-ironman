package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.ENamedElement;

public class MapItemElement {
	
	private String item;
	//TODO change ENamedElement to EStructuralFeature...
	private ENamedElement readElement;
	private ENamedElement writeElement;
	
	public MapItemElement() {
		this.item = null;
		this.readElement = null;
		this.writeElement = null;
	}
	
	public MapItemElement(String item, ENamedElement readElement, ENamedElement writeElement) {
		this.item = item;
		this.readElement = readElement;
		this.writeElement = writeElement;
	}
	
	public ENamedElement getReadElement() {
		return readElement;
	}
	
	public ENamedElement getWriteElement() {
		return writeElement;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setReadElement(ENamedElement readElement) {
		this.readElement = readElement;
	}
	
	public void setWriteElement(ENamedElement writeElement) {
		this.writeElement = writeElement;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
}
