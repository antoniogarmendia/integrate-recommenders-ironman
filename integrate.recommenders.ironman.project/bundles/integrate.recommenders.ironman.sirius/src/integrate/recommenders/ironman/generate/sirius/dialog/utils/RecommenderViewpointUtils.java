package integrate.recommenders.ironman.generate.sirius.dialog.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

public final class RecommenderViewpointUtils {
	
	//Instantiability is not allowed
	public RecommenderViewpointUtils() {
		throw new AssertionError();
	}
	
	public static final String RECOMMENDER_SUFFIX = "recommender";
	
	/*This method return all the registered viewpoints 
	 * 
	 * @param The metamodel nsURI
	 * @return the set of viewpoints
	 * 
	 * */
	public static Set<Viewpoint> getAllViewpointbyMetamodelNsURI(String metaModelNsURI) {		
		return ViewpointRegistry.getInstance().getViewpoints()
				.stream()
				.filter(vp -> hasDiagramRepresentation(vp,metaModelNsURI))
				.collect(Collectors.toSet())
				;
	}
	
	/*This method return true if the viewpoint contains 
	 * at least one diagram description 
	 * 
	 * @param The metamodel nsURI
	 * @return the set of viewpoints
	 * 
	 * */	
	public static boolean hasDiagramRepresentation(Viewpoint vp, String metaModelNsURI) {
		long count = vp.getOwnedRepresentations()
						.stream()
						.filter(DiagramDescription.class::isInstance)
						.filter(diagDesc -> diagDesc.getMetamodel().stream()
								.filter(pack -> pack.getNsURI().equals(metaModelNsURI)).count() != 0)
						.count();
		return count!=0;
	}
	
	/*Return the plugin name from viewpoint 
	 * 
	 * @param Viewpoint object
	 * @return the plugin name
	 * 
	 * */
	public static String getPluginNameFromViewpoint(Viewpoint viewpoint) {
		final Resource resource = viewpoint.eResource();
		if (resource != null && resource.getURI() != null) {
            final URI vpURI = resource.getURI();
            if (vpURI.isPlatform()) {
                return vpURI.segment(1);                
            }
        }
		return "";
	}
}
