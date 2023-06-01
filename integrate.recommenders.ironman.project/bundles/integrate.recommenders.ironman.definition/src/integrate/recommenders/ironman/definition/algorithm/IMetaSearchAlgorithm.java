package integrate.recommenders.ironman.definition.algorithm;

import java.util.List;
import java.util.Map;

import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;

public interface IMetaSearchAlgorithm {
	
	/*
	 *Return an ordered map with key = name of the recommendation and value = score rating
	 *
	 *@params a Map with key = recommender server URL and a map with key = name of the recommendation service
	 * and Map with key= recommendation and value = score rating
	 * 
	 **/
	public Map<String,Double> algorithm(Map<String, List<ItemRecommender>> recommendations);

}
