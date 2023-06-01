package integrate.recommenders.ironman.wizard.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static integrate.recommenders.ironman.wizard.utils.IronManRestAPIUtils.*;

public class CallAPIJSON<T extends Object>{
	
	public final RestAPIConfiguration configuration;
		
	public CallAPIJSON(final RestAPIConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public String doRequest() {
		try {
			final URL url = new URL(configuration.getUrl() + getParamsString(configuration.getGetParameters()));			
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = null;
			switch (this.configuration.getRequestMethod()) {
				case GET:
					request = doGet(url);
					break;
				case POST:	
					request = doPost(url);
					break;
			}					
			
			CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, BodyHandlers.ofString());
			
			HttpResponse<String> response = futureResponse.get();
			
			if (response.statusCode() == 200) {
				final String jsonResponse = response.body();
				return jsonResponse;
			}				
		} catch (IOException | InterruptedException | ExecutionException e) {
			try {
				Thread.sleep(300);
				//e.printStackTrace(); TODO apply retry pattern
				System.out.println("Try again + " + this.configuration.getBody());
				return doRequest();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}			
		}
		return null;
	}
	
	private HttpRequest doGet(URL url) {
		try {
			return HttpRequest.newBuilder()
					.uri(url.toURI())
					.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getInput());
		}
	}
	
	private HttpRequest doPost(URL url) {
		if (!this.configuration.getBody().isBlank())
			return doPostWithBody(url);
		try {
			return HttpRequest.newBuilder()
					  .uri(url.toURI())
					  .POST(HttpRequest.BodyPublishers.noBody())
					  .build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getInput());
		}
	}
	
	private HttpRequest doPostWithBody(URL url) {
		try {
			return HttpRequest.newBuilder()
					  .uri(url.toURI())
					  .POST(HttpRequest.BodyPublishers.ofString(this.configuration.getBody()))
					  .build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	//TODO retry pattern?
}
