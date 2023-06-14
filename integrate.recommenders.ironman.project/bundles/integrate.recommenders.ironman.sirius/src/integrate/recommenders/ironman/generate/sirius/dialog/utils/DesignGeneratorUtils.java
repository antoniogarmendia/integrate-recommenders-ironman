package integrate.recommenders.ironman.generate.sirius.dialog.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.Activator;

public final class DesignGeneratorUtils {
	
	private DesignGeneratorUtils() {
		throw new AssertionError();
	}
	
	// Base Recommender 
	public static final String BASE_RECOMMENDER = "/skeleton-recommender-design/recommender-skeleton.odesign";
	
	// Base Recommender 
	public static final String VIEWPOINT_RECOMMENDER_NAME = "RecommenderVp";
	
	// Package Actions 
	public static final String PACKAGE_ACTIONS = ".actions";
	
	// Package Utils
	public static final String PACKAGE_UTILS = ".utils";
	
	//Get the skeleton of the recommender odesign
	public static Resource recommenderResource(ResourceSet reset) {
		return reset.getResource(URI.createPlatformPluginURI(Activator.PLUGIN_ID + BASE_RECOMMENDER, false), true);
	}
	
	public static Group getRecommenderDescriptionGroup(ResourceSet reset) {
		Resource variabilityResource = recommenderResource(reset);
		EObject groupEObject = variabilityResource.getContents().get(0);
		if (groupEObject instanceof Group) {
			return (Group) groupEObject;			
		}		
		throw new IllegalAccessError("Cannot find the odesign: " + BASE_RECOMMENDER);
	}
	
	public static Group copyOfBaseRecommenderGroup(ResourceSet reset) {
		final Group  group = getRecommenderDescriptionGroup(reset);
		final Copier copier = new Copier();
		final Group resultGroup = (Group) copier.copy(group);
		copier.copyReferences();		
		return resultGroup;
	}
	
	/*Returns the viewpoint uri from DiagramDescription 
	 * 
	 * @param DiagramDescription
	 * @returns ViewpointURI with syntax viewpoint:/pluginId/ViewpointName
	 * */
	public static String getViewpointURIFromDiagramDescription(DiagramDescription diagramDescription) {
		final Viewpoint vp = ViewpointRegistry.getInstance().getViewpoint(diagramDescription);		
		final String projectName = getPluginNameFromViewpoint(vp);
		return "viewpoint:/" + projectName + "/" + vp.getName();
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

	public static Set<String> getAllItems(Map<String, List<Service>> recommenderToServices) {
		final Set<String> setOfItems = new HashSet<String>();
		for (Map.Entry<String, List<Service>> entry : recommenderToServices.entrySet()) {
			final List<Service> listOfServices = entry.getValue();
			for (Service service : listOfServices) {
				for (Recommender recommend : service.getServices()) {
					for(String item:  recommend.getDetails().getItems()) {
						setOfItems.add(item);						
					}					
				}
			}			
		}		
		return setOfItems;
	}	
}
