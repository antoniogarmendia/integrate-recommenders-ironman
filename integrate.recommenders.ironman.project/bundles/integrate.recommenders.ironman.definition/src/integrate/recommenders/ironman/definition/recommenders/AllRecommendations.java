package integrate.recommenders.ironman.definition.recommenders;

import java.util.ArrayList;
import java.util.List;

public class AllRecommendations {
	
	private List<ItemRecommender> recommendations = new ArrayList<ItemRecommender>();
	
	public List<ItemRecommender> getRecommendations() {
		return recommendations;
	}
	
	public void setRecommendations(List<ItemRecommender> recommendations) {
		this.recommendations = recommendations;
	}
}
