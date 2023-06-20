package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;

public class Detail {
	
	private String nsURI;
	private boolean context;
	private String source;
	private String id;
	private ArrayList<Item> items;
	private String target;
	
	public String getNsURI() {
		return nsURI;
	}
	
	public boolean hasContext() {
		return context;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getId() {
		return id;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setNsURI(String nsURI) {
		this.nsURI = nsURI;
	}
	
	public void setContext(boolean context) {
		this.context = context;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
}
