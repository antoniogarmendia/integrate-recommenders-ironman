package integrate.recommenders.ironman.definition.recommendation;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private String name;
	private List<SpecAttribute> eAttributes;
	
	public Target() {
		this.eAttributes = new ArrayList<SpecAttribute>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<SpecAttribute> getEAttributes() {
		return eAttributes;
	}
	
	public void setEAttributes(ArrayList<SpecAttribute> eAttributes) {
		this.eAttributes = eAttributes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
