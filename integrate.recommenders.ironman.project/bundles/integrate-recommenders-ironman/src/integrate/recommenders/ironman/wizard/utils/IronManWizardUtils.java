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
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

public final class IronManWizardUtils {
	
	//Supress default constructor for noninstantiability
	public IronManWizardUtils() {
		throw new AssertionError();
	}
	
	public static final String IRONMAN_WIZARD_NAME = "IronMan Project";
	
	public static final String IRONMAN_WIZARD_PAGE_SELECT_RECOMMENDER_NAME = "Select Available Recommenders";
	
	public static final String IRONMAN_WIZARD_PAGE_SELECT_TARGET_ITEMS_NAME = "Select Available Target & Items";
	
	public static final String IRONMAN_WIZARD_PAGE_CONFIGURE_MODELLING_LANGUAGE = "Modelling Language Configuration";
	
	public static final String IRONMAN_WIZARD_PAGE_AGGREGATION_METHOD_NAME = "Select Aggregation Method";
	
	public static final String IRONMAN_WIZARD_PAGE_CONFIGURE_MODELLING_NAME = "Modelling Tools Integration";
	
	
	public static void treeViewerStyle(TreeViewer treeViewer) {
		final Color backGround = new Color(Display.getDefault(), 220, 220, 220);
		treeViewer.getTree().setHeaderBackground(backGround);	
		//Bold Header
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(new FontData())
				.setStyle(SWT.BOLD)
				.setHeight(9);
	    Font boldFont = boldDescriptor.createFont(Display.getCurrent());
	    treeViewer.getTree().setFont(boldFont); 
	}
	
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
			final String jsonResponse = callAPI.doRequest();			
			try {
				final AllRecommenders allRecommenders = objectMapper.readValue(jsonResponse, AllRecommenders.class);
				recommenderToServices.put(url, allRecommenders.getServices());			
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
	
	//Utility methods for TreeViewer
	public static SelectionAdapter selectTreeViewerItem() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;
				if (item.getChecked() == true) {
					selectAllTreeItemChildren(item);	
					selectParentIfNotChecked(item);
				} else {
					deselectAllTreeItemChildren(item);
					deselectParentIfAnyChildSelected(item);
				}
			}			
		};
	}
	
	public static void selectParentIfNotChecked(TreeItem item) {
		if (item.getParentItem() != null) {
			if (item.getParentItem().getChecked() == false) {
				item.getParentItem().setChecked(true);
				selectParentIfNotChecked(item.getParentItem());
			}		
		}
	}

	public static void deselectParentIfAnyChildSelected(TreeItem item) {
		boolean isAtLeastOneItemChecked = false;
		if (item.getParentItem() != null) {
			for (TreeItem treeItemChild : item.getParentItem().getItems()) {
				if (treeItemChild.getChecked() == true) {
					isAtLeastOneItemChecked = true;
					break;
				}					
			}
			if (isAtLeastOneItemChecked == false) {
				item.getParentItem().setChecked(false);
				deselectParentIfAnyChildSelected(item.getParentItem());
			}
		}
	}

	public static void selectAllTreeItemChildren(final TreeItem item) {
		for (TreeItem childItem: item.getItems()) {
			childItem.setChecked(true);	
			selectAllTreeItemChildren(childItem);
		}
	}
	
	public static void deselectAllTreeItemChildren(final TreeItem item) {
		for (TreeItem childItem: item.getItems()) {
			childItem.setChecked(false);	
			deselectAllTreeItemChildren(childItem);
		}
	}
}
