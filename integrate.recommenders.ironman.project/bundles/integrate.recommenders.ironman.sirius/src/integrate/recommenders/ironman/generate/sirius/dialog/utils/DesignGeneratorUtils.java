package integrate.recommenders.ironman.generate.sirius.dialog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import integrate.recommenders.ironman.definition.services.Item;
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
	
	// Package dialog
	public static final String PACKAGE_DIALOG = ".dialog";
	
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
	
	//Return all the items (String)
	public static Set<String> getAllStringItems(Map<String, List<Service>> recommenderToServices) {
		final Set<String> setOfItems = new HashSet<String>();
		for (Map.Entry<String, List<Service>> entry : recommenderToServices.entrySet()) {
			final List<Service> listOfServices = entry.getValue();
			for (Service service : listOfServices) {
				final List<String> listOfItems = service.getDetail().getItems()
														.stream().map(i -> i.getRead())
														.toList();
				setOfItems.addAll(listOfItems);			
			}			
		}		
		return setOfItems;
	}	
	
	//Return all the Item object
	public static Map<Item,List<Service>> getAllItems(Map<String, List<Service>> recommenderToServices) {
		final Map<Item,List<Service>> itemToServices = new HashMap<Item,List<Service>>();
		for (Map.Entry<String, List<Service>> entry : recommenderToServices.entrySet()) {
			final List<Service> listOfServices = entry.getValue();
			for (Service service : listOfServices) {
				for (Item item : service.getDetail().getItems()) {
					final Optional<Item> optionalItem = 
							itemToServices.keySet().stream().filter(i -> i.getRead().equals(item.getRead())).findAny();
					if (!optionalItem.isPresent()) {
						final List<Service> services = new ArrayList<Service>();
						services.add(service);
						itemToServices.put(item,services);
					} else {
						itemToServices.get(optionalItem.get()).add(service);						
					}
				}							
			}			
		}		
		return itemToServices;
	}
	
	public static void treeViewerStyle(TreeViewer treeViewer) {
		final Color backGround = new Color(Display.getDefault(), 220, 220, 220);
		treeViewer.getTree().setHeaderBackground(backGround);	
		//Bold Header
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(new FontData())
				.setStyle(SWT.BOLD)
				.setHeight(9);
	    Font boldFont = boldDescriptor.createFont(Display.getCurrent());
	    treeViewer.getTree().setFont(boldFont); 
	}
}
