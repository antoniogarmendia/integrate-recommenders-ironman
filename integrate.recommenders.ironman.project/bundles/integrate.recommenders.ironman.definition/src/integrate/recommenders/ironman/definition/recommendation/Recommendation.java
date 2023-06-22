package integrate.recommenders.ironman.definition.recommendation;

public class Recommendation {
	
	private Target target;
    private Context context;
    
    public Context getContext() {
		return context;
	}
    
    public Target getTarget() {
		return target;
	}
    
    public void setContext(Context context) {
		this.context = context;
	}
    
    public void setTarget(Target target) {
		this.target = target;
	}
    
}
