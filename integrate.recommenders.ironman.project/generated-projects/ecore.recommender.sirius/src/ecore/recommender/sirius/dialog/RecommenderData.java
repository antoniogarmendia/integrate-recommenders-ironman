package ecore.recommender.sirius.dialog;

public class RecommenderData {
	
	private String name;
	private String recommenders;
	private String rating;
	
	public RecommenderData(String name, String recommenders, String rating) {
		this.name = name;
		this.recommenders = recommenders;
		this.rating = rating;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRecommenders() {
		return recommenders;
	}
	
	public String getRating() {
		return rating;
	}
}		
