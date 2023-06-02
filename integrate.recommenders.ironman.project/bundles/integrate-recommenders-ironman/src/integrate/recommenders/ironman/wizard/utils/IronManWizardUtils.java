package integrate.recommenders.ironman.wizard.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import integrate.recommenders.ironman.definition.services.AllRecommenders;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.wizard.Activator;
import integrate.recommenders.ironman.wizard.preferences.IronManPreferenceConstants;
import integrate.recommenders.ironman.wizard.rest.api.CallAPIJSON;
import integrate.recommenders.ironman.wizard.rest.api.REQUEST_METHOD;
import integrate.recommenders.ironman.wizard.rest.api.RestAPIConfiguration;

import static integrate.recommenders.ironman.wizard.utils.IronManRestAPIUtils.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

public final class IronManWizardUtils {
	
	//Supress default constructor for noninstantiability
	public IronManWizardUtils() {
		throw new AssertionError();
	}
	
	public static final String IRONMAN_WIZARD_NAME = "IronMan Project";
	
	public static final String IRONMAN_WIZARD_PAGE_SELECT_RECOMMENDER_NAME = "Select Available Recommenders";
	
	/*
	 * this returns a map of key = URL of the server and as value the object Service	 * 
	 * */
	
	public static final Map<String, List<Service>> getAllRecommender() {
		final String[] recommenders = getDefinedURL();
		final Map<String,String> param = new HashMap<String, String>();
		param.put(NSURI_PARAM,"true");
		final Map<String,List<Service>> recommenderToServices = new HashMap<String, List<Service>>();
		for (String url : recommenders) {
			final RestAPIConfiguration config = new RestAPIConfiguration
					.Builder(url + SERVICES_URL + "?", REQUEST_METHOD.GET)
					.setParameters(param)
					.build();
			CallAPIJSON<AllRecommenders> callAPI = new CallAPIJSON<AllRecommenders>(config);
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();
			final String jsonResponse = callAPI.doRequest();			;
			try {
				final AllRecommenders allRecommenders = objectMapper.readValue(jsonResponse, AllRecommenders.class);
				recommenderToServices.put(url, allRecommenders.getRecommenders());			
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return Collections.unmodifiableMap(recommenderToServices);		
	}
	
	public static String[] getDefinedURL() {
		final String servers =  ironManPreferences().getString(IronManPreferenceConstants.IRONMAN_LIST_SERVERS);
		return servers.split("\\|");
	}
	
	public static IPreferenceStore ironManPreferences() {
		return Activator.getDefault().getPreferenceStore();
	}

}
