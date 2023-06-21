package integrate.recommenders.ironman.definition.services;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;

public class Detail {
	
	private String nsURI;
	private boolean context;
	private String source;
	private String id;
	private ArrayList<Item> items;
	private String target;
	private EPackage ePackage = null;
	private EClass targetEClass = null;

	public Detail() {
		// Do nothing
	}
	
	public Detail(String nsURI, boolean context, String source, String id, String target) {
		this.nsURI = nsURI;
		this.context = context;
		this.source = source;
		this.id = id;
		this.target = target;
		this.items = new ArrayList<Item>();
	}

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
	
	public EPackage getEPackage() {
		if (ePackage == null) {
			this.ePackage = EPackageRegistryImpl.INSTANCE.getEPackage(this.nsURI);
		}
		return this.ePackage;
	}
	
	public EClass getTargetEClass() {
		if(this.targetEClass == null) {
			this.targetEClass = this.getEPackage()
									.getEClassifiers().stream()
									.filter(eClassifier -> eClassifier.getName().equals(this.target))
									.findAny()
									.map(EClass.class::cast)
									.orElseThrow()
									;	
		}			
		return this.targetEClass;	
	}
}
