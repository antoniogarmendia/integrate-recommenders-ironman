package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;
import java.util.List;

public class AllRecommenders {
	
	//Services
	private List<Service> services = new ArrayList<Service>();
	
	public void setRecommenders(List<Service> services) {
		this.services = services;
	}
	
	public List<Service> getServices() {
		return services;
	}
}
