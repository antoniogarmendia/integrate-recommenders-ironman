package integrate.recommenders.ironman.definition.recommendation;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private List<SpecAttribute> eAttributes;
	
	public Target() {
		this.eAttributes = new ArrayList<SpecAttribute>();
	}
	
	public List<SpecAttribute> getEAttributes() {
		return eAttributes;
	}
	
	public void setEAttributes(ArrayList<SpecAttribute> eAttributes) {
		this.eAttributes = eAttributes;
	}
}
