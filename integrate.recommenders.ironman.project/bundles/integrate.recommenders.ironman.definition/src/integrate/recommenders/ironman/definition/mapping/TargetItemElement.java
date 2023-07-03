package integrate.recommenders.ironman.definition.mapping;

public class TargetItemElement {
	
	private AbstractItemElement read;
	private AbstractItemElement write;
	private AbstractItemElement feature;
	private String className;
	
	public TargetItemElement() {
		// Do nothing
	}
	
	public TargetItemElement(AbstractItemElement read, AbstractItemElement write, AbstractItemElement feature, 
			String className) {
		this.read = read;
		this.write = write;
		this.feature = feature;
		this.className = className;
	}

	public AbstractItemElement getRead() {
		return read;
	}
	
	public AbstractItemElement getWrite() {
		return write;
	}
	
	public AbstractItemElement getFeature() {
		return feature;
	}
	
	public void setRead(AbstractItemElement read) {
		this.read = read;
	}
	
	public void setWrite(AbstractItemElement write) {
		this.write = write;
	}
	
	public void setFeature(AbstractItemElement feature) {
		this.feature = feature;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
}
