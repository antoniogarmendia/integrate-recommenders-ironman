package integrate.recommenders.ironman.generate.sirius.generator.template

import project.generator.api.template.IGeneration
import java.util.Map
import java.util.List
import integrate.recommenders.ironman.definition.services.Service

class RecommenderUtils implements IGeneration {
	
	val String packageName;
	val Map<String, List<Service>> recommenderToServices;
	
	new(String packageName, Map<String, List<Service>> recommenderToServices) {
		this.packageName = packageName;
		this.recommenderToServices = recommenderToServices;
	}
	
	override doGenerate() {
		'''
			package «packageName»;
			
			import java.util.Collection;
			import java.util.Collections;
			import java.util.HashMap;
			import java.util.List;
			import java.util.Map;
			import java.util.stream.Collectors;
			
			import com.fasterxml.jackson.core.JsonProcessingException;
			import com.fasterxml.jackson.databind.ObjectMapper;
			
			import integrate.recommenders.ironman.wizard.rest.api.CallAPIJSON;
			import integrate.recommenders.ironman.wizard.rest.api.REQUEST_METHOD;
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
			}			
		'''
	}
	
	def recommenderURLs() {
		'''
		«var i = 1»
		«FOR Map.Entry<String, List<Service>> entryRecommend : recommenderToServices.entrySet»
			public static final String URL_RECOMMENDER_«i++» = "«entryRecommend.key»";
			«FOR services: entryRecommend.value»
				«FOR service: services.services»
				public static final String RECOMMENDER_«i»_«service.name» = "«service.name»";
				«ENDFOR»
			«ENDFOR»			
		«ENDFOR»
		'''
	}
	
	def getAllRecommendations() {
		'''
		
		public static Map<String, List<ItemRecommender>> getAllRecommendations(RecommenderCase recommenderCase) {
			final String body = "{\"name\": \"" + recommenderCase.getTargetName() + "\"}";
			final Map<String, Collection<String>> urlRecommenders = recommenderCase.getUrlToRecommenders();
			final Map<String, List<ItemRecommender>> recServerToItemRecommenders = new HashMap<String,List<ItemRecommender>>();
			for (Map.Entry<String, Collection<String>> entry : urlRecommenders.entrySet()) {
				final String recommenderURL = entry.getKey();
				final Collection<String> collectionOfRecommenders = entry.getValue();
				final AllRecommendations recommendations = getRecommendations(recommenderURL, 
						String.join("," , collectionOfRecommenders), body, recommenderCase.getType());
				final List<ItemRecommender> itemRecommenders = filterEmptyRecommendations(recommendations);			
				recServerToItemRecommenders.put(recommenderURL,itemRecommenders);					
			}
			return recServerToItemRecommenders;
		}
		'''
	}
	
	def getRecommendations() {
		'''
		
		private static AllRecommendations getRecommendations(String url, String services, String body, 
				String type) {
			final Map<String,String> param = new HashMap<String, String>();
			param.put(SERVICES_URL, services);
			param.put(ITEM_TYPE_PARAM, type);
			
			final RestAPIConfiguration config = new RestAPIConfiguration
					.Builder(url + RECOMMENDER_URL + "?", REQUEST_METHOD.POST)
					.setParameters(param)
					.setBody(body)
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
}