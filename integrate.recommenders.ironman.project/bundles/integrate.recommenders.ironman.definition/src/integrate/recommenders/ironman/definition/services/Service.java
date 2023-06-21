package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;
import java.util.List;

public class Service {	
	
	private String name;
	private List<Detail> details;
	
	public Service() {
		// Do nothing
	}
	
	public Service(String name) {
		this.name = name;
		this.details = new ArrayList<Detail>();
	}
	
	public Service(Service service) {
		this.name = service.getName();
		this.details = new ArrayList<Detail>();
		final Detail detail = new Detail(service.getDetail().getNsURI(),
				service.getDetail().hasContext(), service.getDetail().getSource(),
				service.getDetail().getId(), service.getDetail().getTarget());
		this.details.add(detail);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Detail> getDetails() {
		return details;
	}
	
	public Detail getDetail() {
		return getDetails().get(0);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDetails(List<Detail> details) {
		this.details = details;
	}
}