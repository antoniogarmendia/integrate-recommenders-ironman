package integrate.recommenders.ironman.definition.mapping;

import org.eclipse.emf.ecore.EClass;

public class TargetElement {
	
	private final String sourceElement;
	private EClass targetElement;
	
	public TargetElement(String sourceElement) {
		this.sourceElement = sourceElement;		
	}

	public String getSourceElement() {
		return sourceElement;
	}
	
	public EClass getTargetElement() {
		return targetElement;
	}
	
	public void setTargetElement(EClass targetElement) {
		this.targetElement = targetElement;
	}	
}
