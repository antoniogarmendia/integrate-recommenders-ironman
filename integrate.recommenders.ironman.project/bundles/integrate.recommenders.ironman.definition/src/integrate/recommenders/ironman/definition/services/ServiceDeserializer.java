package integrate.recommenders.ironman.definition.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceDeserializer extends JsonDeserializer<Service>{

	@Override
	public Service deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
		
		final JsonNode serviceNode = parser.readValueAsTree();
		
		Service service = new Service();
		service.setNsURI(serviceNode.get("nsURI").asText());
		
		JsonNode servicesNode = serviceNode.get("services");
		if (servicesNode.isArray()) {
			final List<Recommender> listOfRecommenders = new ArrayList<Recommender>();
			for (final JsonNode objNode : servicesNode) {
				final Recommender recommender = new Recommender();
				recommender.setService(service);
				recommender.setName(objNode.get("name").asText());
				final ObjectMapper objectMapper = new ObjectMapper();
				final TargetItems targetItems = objectMapper.readValue(objNode.get("details").toString(),TargetItems.class);
				targetItems.setService(service);
				recommender.setTargetItems(targetItems);
				listOfRecommenders.add(recommender);
		    }
			service.setServices(listOfRecommenders);
		}	
		return service;
	}

	

}
