package integrate.recommenders.ironman.wizard.rest.api;

import java.util.HashMap;
import java.util.Map;

public class RestAPIConfiguration {
	
	// Required parameters
	private final String url;
	private final REQUEST_METHOD requestMethod;
		
	// Optional parameters
	private final Map<String, String> getParameters;
	private final Map<String, String> postParameters;
	private final String body;
	
	private RestAPIConfiguration(Builder builder) {
		this.url = builder.getUrl();
		this.requestMethod = builder.getRequestMethod();
		this.getParameters = builder.getGetParameters();
		this.postParameters = builder.getPostParameters();	
		this.body = builder.body;
	}
	
	public String getUrl() {
		return url;
	}
	
	public REQUEST_METHOD getRequestMethod() {
		return requestMethod;
	}
	
	public Map<String, String> getPostParameters() {
		return postParameters;
	}
	
	public Map<String, String> getGetParameters() {
		return getParameters;
	}	
	
	public String getBody() {
		return body;
	}
	
	public static class Builder {
		
		// Required parameters
		private final String url;
		private final REQUEST_METHOD requestMethod;
				
		// Optional parameters
		private Map<String, String> getParameters = new HashMap<String, String>();
		private Map<String, String> postParameters = new HashMap<String, String>();
		private String body;
				
		public Builder(final String url, final REQUEST_METHOD requestMethod) {
			this.url = url;
			this.requestMethod = requestMethod;	
			this.body = "";
		}
		
		public Builder setParameters(final Map<String, String> getParameters) {
			this.getParameters = getParameters;
			return this;
		}
		
		public Builder postParameters(final Map<String, String> postParameters) {
			this.postParameters = postParameters;
			return this;
		}
		
		public Builder setBody(final String body) {
			this.body = body;
			return this;
		}
		
		public RestAPIConfiguration build() {
			return new RestAPIConfiguration(this);
		}
		
		public String getUrl() {
			return url;
		}
		
		public REQUEST_METHOD getRequestMethod() {
			return requestMethod;
		}
		
		public Map<String, String> getGetParameters() {
			return getParameters;
		}
		
		public Map<String, String> getPostParameters() {
			return postParameters;
		}
	}
}
