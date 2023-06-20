package integrate.recommenders.ironman.definition.services;

@Deprecated
public class Recommender {
	
	private Service service;
	private String name;	
	private TargetItems details;
	
	public Recommender() {
		// Do nothing
	}
	
	public Recommender(Service service, String name, TargetItems details) {
		this.service = service;
		this.name = name;
		this.details = details;
	}
	
	public Recommender(Recommender recommender) {
		this(recommender.getService(), recommender.getName(), recommender.getDetails());
	}
	
	public Service getService() {
		return service;
	}
	
	public String getName() {
		return name;
	}
	
	public TargetItems getDetails() {
		return details;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTargetItems(TargetItems details) {
		this.details = details;
	}
	
	public void setService(Service service) {
		this.service = service;
	}	
}
