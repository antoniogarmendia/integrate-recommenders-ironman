package project.generator.api.utils;

import org.eclipse.emf.codegen.ecore.genmodel.GenClassifier;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class GenModelUtils {
	
	private GenModelUtils() {
		throw new AssertionError();
	}
	
	public static String getProjectFromEClass(EClass eClass) {
		var genModelURI = EcorePlugin.getEPackageNsURIToGenModelLocationMap(true).get(eClass.getEPackage().getNsURI());
		if (genModelURI.segmentCount() > 1) {
			return genModelURI.segment(1);				
		}
		return null;
	}
	
	public static String getPackageClassFromEClassifier(EClassifier classifier) {
		var genModelURI = EcorePlugin.getEPackageNsURIToGenModelLocationMap(false).get(classifier.getEPackage().getNsURI());
		var genModelResource = new ResourceSetImpl().getResource(genModelURI, true);
		GenModel genModel = (GenModel) EcoreUtil.getObjectByType(genModelResource.getContents(), GenModelPackage.Literals.GEN_MODEL);
		GenClassifier genClassifier = genModel.findGenClassifier(classifier);		
		return genClassifier.getImportedInstanceClassName();
	}
}
