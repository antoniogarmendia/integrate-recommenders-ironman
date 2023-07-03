package uml.recommender.sirius.utils;

import java.util.Collection;
import java.util.Map;

public class RecommenderCase {
	
	private Map<String, Collection<String>> urlToRecommenders;
	private String type;
	private String body;
	
	public RecommenderCase(Map<String, Collection<String>> urlToRecommenders, String type, String body) {
		this.urlToRecommenders = urlToRecommenders;
		this.type = type;
		this.body = body;
	}

	public String getType() {
		return type;
	}
	
	public Map<String, Collection<String>> getUrlToRecommenders() {
		return urlToRecommenders;
	}	
	
	public String getBody() {
		return body;
	}
}
