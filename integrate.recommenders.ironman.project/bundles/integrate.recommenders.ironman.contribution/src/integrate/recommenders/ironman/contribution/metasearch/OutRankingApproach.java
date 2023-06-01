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

public class OutRankingApproach implements IMetaSearchAlgorithm {
	
	//Cmin
	public static final double CONCORDANCE_THRESHOLD = 0.50;
	//Dmax
	public static final double DISCORDANCE_THRESHOLD = 0;
	//Sp
	public static final double PREFERENCE_THRESHOLD = 0;
	//Sv
	public static final double VETO_THRESHOLD = 0.75;
	
	public OutRankingApproach() {
		// Do nothing
	}

	@Override
	public Map<String, Double> algorithm(Map<String, List<ItemRecommender>> recommendations) {
		//Input List Of Recommendations. 
		//The boolean parameter is true if this item belong to the recommender, otherwise, it will be false.
		final List<LinkedHashMap<String, Boolean>> listOfRecommendations 
						= new ArrayList<LinkedHashMap<String,Boolean>>();
		//All recommended items
		final HashSet<String> allItemsSet = new HashSet<String>();
		//Flat List of List
		final List<ItemRecommender> listOfRecommenders = recommendations.values().stream().flatMap(List::stream)
        	.collect(Collectors.toList());
		inputRecommendations(listOfRecommendations, allItemsSet, listOfRecommenders);
		
		addAllMissingRecommendations(listOfRecommendations,allItemsSet);
		
		final List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore
		= new ArrayList<LinkedHashMap<String,Double>>();
		
		addRanking(listOfRecommendations,listOfRecommendationsWithScore);
		
		return mergeRecommendationsOutRankApproach(allItemsSet, listOfRecommendationsWithScore);
	}

	private Map<String, Double> mergeRecommendationsOutRankApproach(
			HashSet<String> allItemsSet, List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore) {
		
		final Map<String, Double> mergeRecommendations = new HashMap<String, Double>();
		//Variables
		final double currentPreferenceThreshold = PREFERENCE_THRESHOLD*allItemsSet.size();
		final double currentVetoThreshold = VETO_THRESHOLD*allItemsSet.size();
		final double currentConcordanceThreshold = CONCORDANCE_THRESHOLD*allItemsSet.size();
		final double currentDiscordanceThreshold = DISCORDANCE_THRESHOLD*allItemsSet.size();
				
		//OutRank Approach
		for (String recRow : allItemsSet) {
			mergeRecommendations.put(recRow, 0.0);
			for (String recColumn : allItemsSet) {
				if (!recRow.equals(recColumn)) {
					int concordance = concordance(recRow, recColumn, listOfRecommendationsWithScore, currentPreferenceThreshold);
					int discordance = discordance(recRow, recColumn, listOfRecommendationsWithScore, currentVetoThreshold);
					double outranking = outranking(concordance, discordance, currentConcordanceThreshold, currentDiscordanceThreshold);
					Double currentValue = mergeRecommendations.get(recRow);
					mergeRecommendations.put(recRow, outranking+currentValue);
				}
			}
		}			
		return mergeRecommendations;
	}
	
	private double outranking(int concordance, int discordance, 
			double currentConcordanceThreshold, double currentDiscordanceThreshold) {
		if (concordance >= currentConcordanceThreshold && discordance <= currentDiscordanceThreshold)
			return 1;
		return 0;
	}

	private int concordance(String doc1, String doc2, List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore,
			double currentPreferenceThreshold) {
		int concordance = 0;
		for (LinkedHashMap<String, Double> recommender : listOfRecommendationsWithScore) {
			double rec1 = recommender.get(doc1);
			double rec2 = recommender.get(doc2);
			if (rec1 <= rec2 - currentPreferenceThreshold) {
				concordance++;
			}			
		}		
		return concordance;
	}
	
	private int discordance(String doc1, String doc2, List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore, 
			double currentVetoThreshold) {
		int discordance = 0;
		for (LinkedHashMap<String, Double> recommender : listOfRecommendationsWithScore) {
			double rec1 = recommender.get(doc1);
			double rec2 = recommender.get(doc2);
			if (rec1 >= rec2 + currentVetoThreshold) {
				discordance++;
			}			
		}		
		return discordance;	
	}

	private void addRanking(List<LinkedHashMap<String, Boolean>> listOfRecommendations,
			List<LinkedHashMap<String, Double>> listOfRecommendationsWithScore) {
		for (LinkedHashMap<String, Boolean> recommendation : listOfRecommendations) {
			double ranking = 1;
			double length = recommendation.size();
			final LinkedHashMap<String, Double> recommendationWithScore =
					new LinkedHashMap<String, Double>();
			for (Map.Entry<String, Boolean> value : recommendation.entrySet()) {
				String item = value.getKey();
				Boolean belongTo = value.getValue();
				if (belongTo == true) {
					recommendationWithScore.put(item, ranking);
					ranking++;
				} else {
					recommendationWithScore.put(item, (((length+ranking)*(length-ranking+1))/2)/(length-ranking+1));
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

	private void inputRecommendations(List<LinkedHashMap<String, Boolean>> listOfRecommendations,
			HashSet<String> allItemsSet, List<ItemRecommender> listOfRecommenders) {
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
