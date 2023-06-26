package integrate.recommenders.ironman.definition.mapping;

public class ActualFeature extends AbstractItemElement{

	public ActualFeature(TargetItemElement parentTargetItemElement, String item) {
		super(parentTargetItemElement, item);
		parentTargetItemElement.setFeature(this);
	}
}
