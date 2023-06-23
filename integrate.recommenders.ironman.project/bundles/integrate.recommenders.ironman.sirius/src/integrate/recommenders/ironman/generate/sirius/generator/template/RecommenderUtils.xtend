package integrate.recommenders.ironman.generate.sirius.generator.template

import project.generator.api.template.IGeneration
import java.util.Map
import java.util.List
import integrate.recommenders.ironman.definition.services.Service

class RecommenderUtils implements IGeneration {
	
	val String packageName;
	val String packageNameDialog;
	val Map<String, List<Service>> recommenderToServices;
	
	new(String packageName, String packageNameDialog, Map<String, List<Service>> recommenderToServices) {
		this.packageName = packageName;
		this.packageNameDialog = packageNameDialog;
		this.recommenderToServices = recommenderToServices;
	}
	
	override doGenerate() {
		'''
			package «packageName»;
			
			import java.util.ArrayList;
			import java.util.Collection;
			import java.util.Collections;
			import java.util.Comparator;
			import java.util.HashMap;
			import java.util.LinkedHashMap;
			import java.util.List;
			import java.util.Map;
			import java.util.stream.Collectors;
			
			import «this.packageNameDialog».RecommenderData;
			
			import com.fasterxml.jackson.core.JsonProcessingException;
			import com.fasterxml.jackson.databind.ObjectMapper;
			
			import integrate.recommenders.ironman.wizard.rest.api.CallAPIJSON;
			import integrate.recommenders.ironman.wizard.rest.api.REQUEST_METHOD;
			import integrate.recommenders.ironman.definition.recommendation.RecommendationRequest;
			import integrate.recommenders.ironman.definition.recommenders.AllRecommendations;
			import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
			import static integrate.recommenders.ironman.wizard.utils.IronManRestAPIUtils.*;
			import integrate.recommenders.ironman.wizard.rest.api.RestAPIConfiguration;
			import integrate.recommenders.ironman.definition.services.AllRecommenders;
						
			public final class RecommenderUtils {
				
				private RecommenderUtils() {
					throw new AssertionError("This class does not allow inheritance");
				}
				//URLs of the Recommenders
				«recommenderURLs»
				«getAllRecommendations»
				«recommendations»
				«filterEmptyRecommendations»
				«normalization»
				«convertToOrderedList»
				«recommenders»				
			}			
		'''
	}
	
	def recommenderURLs() {
		'''
		«var i = 1»
		«FOR Map.Entry<String, List<Service>> entryRecommend : recommenderToServices.entrySet»
			public static final String URL_RECOMMENDER_«i++» = "«entryRecommend.key»";
			«FOR service: entryRecommend.value»
				public static final String RECOMMENDER_«i»_«service.name» = "«service.name»";
			«ENDFOR»			
		«ENDFOR»
		'''
	}
	
	def getAllRecommendations() {
		'''
		
		public static Map<String, List<ItemRecommender>> getAllRecommendations(RecommenderCase recommenderCase) {
			final Map<String, Collection<String>> urlRecommenders = recommenderCase.getUrlToRecommenders();
			final Map<String, List<ItemRecommender>> recServerToItemRecommenders = new HashMap<String,List<ItemRecommender>>();
			for (Map.Entry<String, Collection<String>> entry : urlRecommenders.entrySet()) {
				final String recommenderURL = entry.getKey();
				final Collection<String> collectionOfRecommenders = entry.getValue();
				//Search per recommender and then, join
				for (String recommenderName : collectionOfRecommenders) {
					final AllRecommendations recommendations = getRecommendations(recommenderURL, 
							recommenderName, recommenderCase);
					if (recommendations != null) {
						final List<ItemRecommender> itemRecommenders = filterEmptyRecommendations(recommendations);			
						recServerToItemRecommenders.put(recommenderURL,itemRecommenders);
					}			
				}		
			}
			return recServerToItemRecommenders;
		}
		'''
	}
	
	def getRecommendations() {
		'''
		
		private static AllRecommendations getRecommendations(String url, String service, RecommenderCase recommenderCase) {
			final Map<String,String> param = new HashMap<String, String>();
			param.put(ITEM_TYPE_PARAM, recommenderCase.getType());
						
			final RestAPIConfiguration config = new RestAPIConfiguration
					.Builder(url + "/"+ RECOMMENDER_URL + "/" + service + "?", REQUEST_METHOD.POST)
					.setParameters(param)
					.setBody(recommenderCase.getBody())
					.build();
			
			CallAPIJSON<AllRecommenders> callAPI = new CallAPIJSON<AllRecommenders>(config);
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();
			final String jsonResponse = callAPI.doRequest();
			try {
				return objectMapper.readValue(jsonResponse, AllRecommendations.class);			
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}		
			return null;
		}
		'''
	}
	
	def filterEmptyRecommendations() {
		'''
		
		public static List<ItemRecommender> filterEmptyRecommendations(AllRecommendations recommendations) {
				 final List<ItemRecommender> itemRecommenders = recommendations.getRecommendations()
						 .stream().filter(r-> !r.getItems().isEmpty())
						 .collect(Collectors.toList());		 
				 return itemRecommenders;
			}
		'''
	}
	
	def String normalization() {
		'''
		
		public static Map<String, Double> normalization(Map<String, Double> dataFusion) {
			double min = Collections.min(dataFusion.values());
			double max = Collections.max(dataFusion.values());
			Map<String, Double> recToNormalizeRating = new HashMap<String, Double>();
			for(Map.Entry<String,Double> metaSearchEntry : dataFusion.entrySet()) {
				double normalizeValue = 1.0;//All ranking are the same
				if (min != max) {
					normalizeValue = (metaSearchEntry.getValue() - min)/(max-min);
				}
				recToNormalizeRating.put(metaSearchEntry.getKey(), normalizeValue);
			}	
			return recToNormalizeRating;
		}
		'''
	}	
	
	def String convertToOrderedList() {
		'''
		public static List<RecommenderData> convertToOrderedListOfRecommendations(Map<String, List<ItemRecommender>> recServerToItemRecommenders, 
				Map<String, Double> normalizeDataFusion){
			final List<RecommenderData> listOfRecommendations = new ArrayList<RecommenderData>();
				
			final LinkedHashMap<String, Double> sortedNormalizeMap = normalizeDataFusion.entrySet()
																	  	.stream()
																	  	.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
																	  	.collect(Collectors.toMap(
																	  	        Map.Entry::getKey,
																	  	        Map.Entry::getValue,
																	  	        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			
			for (Map.Entry<String, Double> entry : sortedNormalizeMap.entrySet()) {
				final String recommendation = entry.getKey();
				final double rating = entry.getValue();
				final String recommenders = recommenders(recServerToItemRecommenders,recommendation);			
				RecommenderData recData = new RecommenderData(recommendation, recommenders, Double.toString(rating));
				listOfRecommendations.add(recData);		
			}		
			return listOfRecommendations;
		}
		'''
	}
	
	def String recommenders() {
		'''
		
		private static String recommenders(Map<String, List<ItemRecommender>> recServerToItemRecommenders,
					String recommendation) {
			final StringBuilder recommenders = new StringBuilder();
			String SEPARATOR = "";
			for (Map.Entry<String, List<ItemRecommender>> entry : recServerToItemRecommenders.entrySet()) {
				final List<ItemRecommender> itemsOfRecommenders = entry.getValue();			
				for (ItemRecommender itemRecommender : itemsOfRecommenders) {
					boolean isPresent = !itemRecommender.getItems()
												.stream()
												.filter(item -> item.getPk().get("name").equals(recommendation))
												.findAny()
												.isEmpty();
					if (isPresent) {
						recommenders.append(SEPARATOR);
						recommenders.append(itemRecommender.getName());	
						SEPARATOR = ",";
					}				
				}			
			}		
			return recommenders.toString();
		}
		'''
	}
}