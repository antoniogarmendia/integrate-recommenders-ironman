package integrate.recommenders.ironman.generate.tree.editor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import integrate.recommenders.ironman.definition.services.Item;
import integrate.recommenders.ironman.definition.services.Service;

public class TreeEditorUtils {
	
	private TreeEditorUtils() {
		throw new AssertionError();
	}
	
	//Return all the Item object
	public static Map<Item,List<Service>> getAllItems(Map<String, List<Service>> recommenderToServices) {
		final Map<Item,List<Service>> itemToServices = new HashMap<Item,List<Service>>();
		for (Map.Entry<String, List<Service>> entry : recommenderToServices.entrySet()) {
			final List<Service> listOfServices = entry.getValue();
			for (Service service : listOfServices) {
				for (Item item : service.getDetail().getItems()) {
					final Optional<Item> optionalItem = 
							itemToServices.keySet().stream().filter(i -> i.getRead().equals(item.getRead())).findAny();
					if (!optionalItem.isPresent()) {
						final List<Service> services = new ArrayList<Service>();
						services.add(service);
						itemToServices.put(item,services);
					} else {
						itemToServices.get(optionalItem.get()).add(service);						
					}
				}							
			}			
		}		
		return itemToServices;
	}
	
}
