package integrate.recommenders.ironman.contribution.metasearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import integrate.recommenders.ironman.definition.algorithm.IMetaSearchAlgorithm;
import integrate.recommenders.ironman.definition.recommenders.Item;
import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;

public class BordaCount implements IMetaSearchAlgorithm {

	public BordaCount() {
		// Do nothing
	}

	@Override
	public Map<String, Double> algorithm(
			Map<String, List<ItemRecommender>> recommendations) {
		//Input List Of Recommendations. 
		//The boolean parameter is true if this item belong to the recommender, otherwise, it will be false.
		final List<LinkedHashMap<String, Boolean>> listOfRecommendations 
						= new ArrayList<LinkedHashMap<String,Boolean>>();
		final List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore
			= new ArrayList<LinkedHashMap<String,Double>>();		
		//All recommended items
		final HashSet<String> allItemsSet = new HashSet<String>();
		//Flat List of List
		final List<ItemRecommender> listOfRecommenders = recommendations.values().stream().flatMap(List::stream)
        	.collect(Collectors.toList());
		inputRecommendations(listOfRecommendations, allItemsSet, listOfRecommenders);
		
		addAllMissingRecommendations(listOfRecommendations,allItemsSet);
		
		addRanking(listOfRecommendations,listOfRecommendationsWithScore);
		
		return mergeRecommendations(listOfRecommendationsWithScore);
	}

	private Map<String, Double> mergeRecommendations(
			List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore) {
		
		final Map<String, Double> mergeRecommendations = new HashMap<String, Double>();
		
		for (LinkedHashMap<String, Double> recommendationWithScore : listOfRecommendationsWithScore) {
			for (Map.Entry<String, Double> recommendation : recommendationWithScore.entrySet()) {
				String recommend = recommendation.getKey();
				Double ranking = recommendation.getValue();
				Double currentValue = mergeRecommendations.get(recommend);
				if (currentValue == null)
					mergeRecommendations.put(recommend, ranking);
				else
					mergeRecommendations.put(recommend, ranking+currentValue);
			}
		}		
		
		return mergeRecommendations;
	}

	private void addRanking(List<LinkedHashMap<String, Boolean>> listOfRecommendations,
			List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore) {
		
		for (LinkedHashMap<String, Boolean> recommendation : listOfRecommendations) {
			double length = recommendation.size();
			final LinkedHashMap<String, Double> recommendationWithScore =
					new LinkedHashMap<String, Double>();
			for (Map.Entry<String, Boolean> value : recommendation.entrySet()) {
				String item = value.getKey();
				Boolean belongTo = value.getValue();
				if (belongTo == true) {
					recommendationWithScore.put(item, length);
					length--;
				} else {
					recommendationWithScore.put(item, ((length*(length+1))/2)/length);
				}					
			}
			listOfRecommendationsWithScore.add(recommendationWithScore);
		}		
	}

	private void addAllMissingRecommendations(List<LinkedHashMap<String, Boolean>> listOfRecommendations,
			HashSet<String> allItemsSet) {
		for (Map<String, Boolean> recommender : listOfRecommendations) {
			for (String itemSet : allItemsSet) {
				Boolean value = recommender.get(itemSet);
				if (value == null)
					recommender.put(itemSet, false);
			}
		}		
	}

	private void inputRecommendations(final List<LinkedHashMap<String, Boolean>> listOfRecommendations, 
			final HashSet<String> allItemsSet,
			final List<ItemRecommender> listOfRecommenders) {
		//Add all items
		for (ItemRecommender itemRecommender : listOfRecommenders) {
			//Create HashSet
			final LinkedHashMap<String,Boolean> allRecommendations = new LinkedHashMap<String,Boolean>();
			for (Item item : itemRecommender.getItems()) {
				final String currentItem = item.getPk().get("name");
				allRecommendations.put(currentItem, true);		
				allItemsSet.add(currentItem);
			}					
			listOfRecommendations.add(allRecommendations);
		}
	}

}
