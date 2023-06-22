package integrate.recommenders.ironman.definition.recommendation;

import com.fasterxml.jackson.databind.JsonNode;

public class Context {
	
	private JsonNode contextJson;
	
	public JsonNode getContextJson() {
		return contextJson;
	}
	
	public void setContextJson(JsonNode contextJson) {
		this.contextJson = contextJson;
	}	
}
