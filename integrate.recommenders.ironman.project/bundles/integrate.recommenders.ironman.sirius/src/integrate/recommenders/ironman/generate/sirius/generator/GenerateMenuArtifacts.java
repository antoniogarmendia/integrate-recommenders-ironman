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

import integrate.recommenders.ironman.definition.services.Service;
import integrate.recommenders.ironman.generate.sirius.api.IStrategyGenerateMenu;

public class GenerateMenuArtifacts  implements IStrategyGenerateMenu {

	@Override
	public void generateMenuArtifacts(String projectName, Group groupBaseRecommender, EList<DiagramDescription> selectedDiagramDesc,
			Map<String,List<Service>> recommenderToServices) {
		refineViewpoint(projectName, groupBaseRecommender, selectedDiagramDesc, recommenderToServices);
	}
	
	private void refineViewpoint(String projectName, final Group groupBaseRecommender, EList<DiagramDescription> selectedDiagramDesc, 
			Map<String,List<Service>> recommenderToServices) {
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
			addMenuForItems(projectName, currentDiagDescrip, recommenderToServices);
		}
		EcoreUtil.remove(diagramDesc);		
	}

	private void addMenuForItems(String projectName, DiagramExtensionDescription currentDiagDescrip, Map<String, List<Service>> recommenderToServices) {
		final String packageName = projectName + PACKAGE_ACTIONS;
		//Get PopupMenu
		final PopupMenu popupMenu = currentDiagDescrip.getLayers().get(0).getToolSections().get(0).getPopupMenus().get(0);
		popupMenu.setLabel("Recommender-" + getTarget(recommenderToServices));
		final Set<String> setOfItems = getAllStringItems(recommenderToServices);
		//Add menu for each item
		for (String item : setOfItems) {
			ExternalJavaAction externalJavaAction = ToolFactory.eINSTANCE.createExternalJavaAction();
			externalJavaAction.setId(packageName + "." + item);
			externalJavaAction.setName(packageName + ".Recommend" + item);
			externalJavaAction.setLabel(getETypefromItem(item, recommenderToServices));
			popupMenu.getMenuItemDescription().add(externalJavaAction);
		}
	}
	
	private String getTarget(Map<String, List<Service>> recommenderToServices) {
		final String target = recommenderToServices.entrySet()
									.stream().findAny().orElseThrow()
									.getValue().get(0).getDetail().getTarget();
		return target;		
	}
	
	private String getETypefromItem(String item, Map<String, List<Service>> recommenderToServices) {
		final EClassifier classifier = getEPackageByNsURI(recommenderToServices).getEClassifier(getTarget(recommenderToServices));
		if (classifier instanceof EClass) {
			final EClass eClass = (EClass) classifier; 
			final EStructuralFeature feature = eClass.getEAllStructuralFeatures()
											.stream()
											.filter(struct -> struct.getName().equalsIgnoreCase(item))
											.findAny().orElseThrow();
			return feature.getEType().getName();
		}
		return null;
	}
	
	private EPackage getEPackageByNsURI(Map<String, List<Service>> recommenderToServices) {
		final String nsURI = recommenderToServices.entrySet().stream().findAny().orElseThrow().getValue().get(0).getDetail().getNsURI();
		Object asd = EPackageRegistryImpl.INSTANCE.getEPackage(nsURI); 
		return EPackageRegistryImpl.INSTANCE.getEPackage(nsURI);		
	}
}
