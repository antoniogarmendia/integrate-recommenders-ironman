package integrate.recommenders.ironman.definition.mapping;

public class TargetItemElement {
	
	private AbstractItemElement read;
	private AbstractItemElement write;
	private AbstractItemElement feature;
	
	public TargetItemElement() {
		// Do nothing
	}
	
	public TargetItemElement(AbstractItemElement read, AbstractItemElement write, AbstractItemElement feature) {
		this.read = read;
		this.write = write;
		this.feature = feature;
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
}
