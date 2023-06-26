package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.EStructuralFeature;

public class AbstractItemElement {
	
	protected final TargetItemElement parentTargetItemElement;
	protected String item;
	protected EStructuralFeature structFeature;
	
	protected AbstractItemElement(TargetItemElement parentTargetItemElement, String item) {
		this.parentTargetItemElement = parentTargetItemElement;
		this.item = item;
	}

	public String getItem() {
		return item;
	}
	
	public EStructuralFeature getStructFeature() {
		return structFeature;
	}
	
	public TargetItemElement getParentTargetItemElement() {
		return parentTargetItemElement;
	}
	
	public void setStructFeature(EStructuralFeature structFeature) {
		this.structFeature = structFeature;
	}
}
