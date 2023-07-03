package integrate.recommenders.ironman.definition.services;

public class Item {
	
	private String features;
	private String read;
	private String write;
	private String className;
	
	public Item() {
		// Do nothing
	}
    
	public Item(Item item) {
		this.features = item.features;
		this.read = item.read;
		this.write = item.write;
		this.className = item.className;
	}
	
    public String getFeatures() {
		return features;
	}
    
    public String getRead() {
		return read;
	}
    
    public String getWrite() {
		return write;
	}
    
    public String getClassName() {
		return className;
	}
    
    public void setFeatures(String features) {
		this.features = features;
	}
    
    public void setRead(String read) {
		this.read = read;
	}
    
    public void setWrite(String write) {
		this.write = write;
	}
    
    public void setClassName(String className) {
		this.className = className;
	}

}
