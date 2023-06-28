package integrate.recommenders.ironman.generate.sirius.api;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.Group;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.services.Service;

public interface IStrategyGenerateMenu {
	
	public void generateMenuArtifacts(String projectName, 
			Group groupBaseRecommender, EList<DiagramDescription> selectedDiagramDesc, Map<String,List<Service>> recommenderToServices, MLMappingConfiguration mapping);
}
