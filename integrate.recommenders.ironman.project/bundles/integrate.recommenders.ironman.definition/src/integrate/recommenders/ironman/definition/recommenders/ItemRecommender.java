package integrate.recommenders.ironman.definition.recommenders;

import java.util.ArrayList;
import java.util.List;

public class ItemRecommender {
	
	//Recommender Name
	private String name;
	//List of Recommender Items
	private List<Item> items = new ArrayList<Item>();
	
	public String getName() {
		return name;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
}
