package integrate.recommenders.ironman.generate.sirius.generator.template

import java.util.List
import java.util.Map
import project.generator.api.template.sirius.ExternalJavaActionTemplate
import integrate.recommenders.ironman.definition.services.Service

class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
	
	val Map<String,List<Service>> recommenderToServices;
		
	new(String className, String packageName, Map<String,List<Service>> recommenderToServices) {
		super(className,packageName);
		this.recommenderToServices = recommenderToServices;
	}
	
}
