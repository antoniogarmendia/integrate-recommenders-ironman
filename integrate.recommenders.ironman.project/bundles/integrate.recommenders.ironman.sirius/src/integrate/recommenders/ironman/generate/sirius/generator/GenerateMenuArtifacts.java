package integrate.recommenders.ironman.generate.sirius.generator;

import static integrate.recommenders.ironman.generate.sirius.dialog.utils.DesignGeneratorUtils.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramExtensionDescription;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.tool.ExternalJavaAction;
import org.eclipse.sirius.viewpoint.description.tool.PopupMenu;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.api.IStrategyGenerateMenu;

public class GenerateMenuArtifacts  implements IStrategyGenerateMenu {

	@Override
	public void generateMenuArtifacts(String projectName, Group groupBaseRecommender, EList<DiagramDescription> selectedDiagramDesc,
			Map<String,List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		refineViewpoint(projectName, groupBaseRecommender, selectedDiagramDesc, recommenderToServices, mapping);
	}
	
	private void refineViewpoint(String projectName, final Group groupBaseRecommender, EList<DiagramDescription> selectedDiagramDesc, 
			Map<String,List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		// Get source diagram extension
		final Viewpoint recommenderVp = groupBaseRecommender.getOwnedViewpoints().get(0);
		recommenderVp.setName(recommenderVp.getName() + " " + projectName);
		final DiagramExtensionDescription diagramDesc = (DiagramExtensionDescription) recommenderVp.getOwnedRepresentationExtensions().get(0);
		for (DiagramDescription diagramDescription : selectedDiagramDesc) {
			final DiagramExtensionDescription currentDiagDescrip = EcoreUtil.copy(diagramDesc);
			currentDiagDescrip.setName("Recommender-" + diagramDescription.getName());
			currentDiagDescrip.setViewpointURI(getViewpointURIFromDiagramDescription(diagramDescription));
			currentDiagDescrip.setRepresentationName(diagramDescription.getName());
			recommenderVp.getOwnedRepresentationExtensions().add(currentDiagDescrip);
			//Add menu to each layer
			addMenuForItems(projectName, currentDiagDescrip, recommenderToServices, mapping);
		}
		EcoreUtil.remove(diagramDesc);		
	}

	private void addMenuForItems(String projectName, DiagramExtensionDescription currentDiagDescrip, Map<String, List<Service>> recommenderToServices, 
			 MLMappingConfiguration mapping) {
		final String packageName = projectName + PACKAGE_ACTIONS;
		//Get PopupMenu
		final PopupMenu popupMenu = currentDiagDescrip.getLayers().get(0).getToolSections().get(0).getPopupMenus().get(0);
		popupMenu.setLabel("Recommender-" + getTarget(recommenderToServices, mapping));
		final Set<String> setOfItems = getAllStringItems(recommenderToServices);
		//Add menu for each item
		for (String item : setOfItems) {
			ExternalJavaAction externalJavaAction = ToolFactory.eINSTANCE.createExternalJavaAction();
			externalJavaAction.setId(packageName + "." + item);
			externalJavaAction.setName(packageName + ".Recommend" + item);
			externalJavaAction.setLabel(getETypefromItem(item, recommenderToServices, mapping));
			popupMenu.getMenuItemDescription().add(externalJavaAction);
		}
	}
	
	private String getTarget(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		if (mapping == null) {
			return recommenderToServices.entrySet()
										.stream().findAny().orElseThrow()
										.getValue().get(0).getDetail().getTarget();
		} else {
			return mapping.getMapTargetElementToTargetItems().entrySet().iterator().next().getKey().getTargetElement().getName();
		}				
	}
	
	private String getETypefromItem(String item, Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		final EClassifier classifier = getEPackageByNsURI(recommenderToServices, mapping).getEClassifier(getTarget(recommenderToServices, mapping));
		if (classifier instanceof EClass) {
			final EClass eClass = (EClass) classifier; 
			final EStructuralFeature feature = eClass.getEAllStructuralFeatures()
											.stream()
											.filter(struct -> struct.getName().equalsIgnoreCase(getActualItem(item,mapping)))
											.findAny().orElseThrow();
			return feature.getEType().getName();
		}
		return null;
	}
	
	private String getActualItem(String item, MLMappingConfiguration mapping) {
		if (mapping == null) {
			return item;
		} else {
			return mapping.getMapTargetElementToTargetItems().values().stream()
						.flatMap(listTargetItems -> listTargetItems.stream())
						.filter(targetItem -> targetItem.getRead().getItem().equals(item))
						.findAny().orElseThrow().getRead().getStructFeature().getName();			
		}		
	}

	private EPackage getEPackageByNsURI(Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		if (mapping != null) {
			return mapping.getEPackage();			
		} else {
			final String nsURI = recommenderToServices.entrySet().stream().findAny().orElseThrow().getValue().get(0).getDetail().getNsURI();
			return EPackageRegistryImpl.INSTANCE.getEPackage(nsURI);			
		}
				
	}
}
