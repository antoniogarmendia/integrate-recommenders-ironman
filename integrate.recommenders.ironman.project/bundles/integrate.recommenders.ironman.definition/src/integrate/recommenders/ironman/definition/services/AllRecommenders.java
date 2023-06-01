package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;
import java.util.List;

public class AllRecommenders {
	
	//Services
	private List<Service> recommenders = new ArrayList<Service>();
	
	public void setRecommenders(List<Service> recommenders) {
		this.recommenders = recommenders;
	}
	
	public List<Service> getRecommenders() {
		return recommenders;
	}
}
