package integrate.recommenders.ironman.definition.mapping;

public class ReadFeature extends AbstractItemElement{

	public ReadFeature(TargetItemElement parentTargetItemElement, String item) {
		super(parentTargetItemElement, item);	
		parentTargetItemElement.setRead(this);
	}
}
