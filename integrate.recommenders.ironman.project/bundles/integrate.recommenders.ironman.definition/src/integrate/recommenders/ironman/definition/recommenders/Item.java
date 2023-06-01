package integrate.recommenders.ironman.definition.recommenders;

import java.util.HashMap;
import java.util.Map;

public class Item {
	
	//Type of the recommendation EAttribute, EOperation, etc..
	private String className;
	//Pk
	private Map<String,String> pk = new HashMap<String,String>();
	//Value of the recommendation
	private double value;
	
	public String getClassName() {
		return className;
	}
	
	public Map<String, String> getPk() {
		return pk;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setPk(Map<String, String> pk) {
		this.pk = pk;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

}
