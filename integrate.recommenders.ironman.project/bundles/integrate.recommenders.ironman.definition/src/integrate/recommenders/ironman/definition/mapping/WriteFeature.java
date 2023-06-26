package integrate.recommenders.ironman.definition.mapping;

public class WriteFeature extends AbstractItemElement{

	public WriteFeature(TargetItemElement parentTargetItemElement, String item) {
		super(parentTargetItemElement, item);	
		parentTargetItemElement.setWrite(this);
	}
}
