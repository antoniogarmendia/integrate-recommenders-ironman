package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.EClass;

public class MapTargetElement {
	
	private String sourceElement;
	private EClass targetElement;
	
	public MapTargetElement(String sourceElement, EClass targetElement) {
		this.sourceElement = sourceElement;
		this.targetElement = targetElement;
	}

	public String getSourceElement() {
		return sourceElement;
	}
	
	public void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
	}
	
	public EClass getTargetElement() {
		return targetElement;
	}
	
	public void setTargetElement(EClass targetElement) {
		this.targetElement = targetElement;
	}	
}
