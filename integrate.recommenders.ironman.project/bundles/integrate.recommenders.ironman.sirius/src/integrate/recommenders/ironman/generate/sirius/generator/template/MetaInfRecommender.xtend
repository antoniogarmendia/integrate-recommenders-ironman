package integrate.recommenders.ironman.generate.sirius.generator.template

import org.eclipse.core.resources.IProject
import project.generator.api.template.MetaInfTemplate
import java.util.Map
import java.util.List
import integrate.recommenders.ironman.definition.services.Service
import integrate.recommenders.ironman.definition.services.Recommender
import org.eclipse.emf.ecore.EClass
import project.generator.api.utils.GenModelUtils

class MetaInfRecommender extends MetaInfTemplate {
	
	val IProject project;
	val Map<String, List<Service>> recommenderToServices;
		
	new(IProject project, Map<String, List<Service>> recommenderToServices) {
		this.project = project;		
		this.recommenderToServices = recommenderToServices;
	}
	
	override bundleNameSymbolic() {
		'''
			Bundle-Name: ProjectTreeIntegration
			Bundle-SymbolicName: «this.project.name»;singleton:=true
		'''
	}
	
	override exportPackage() {
		''
	}
	
	override requireBundle() {
		'''
			Require-Bundle: org.eclipse.ui,
			 org.eclipse.core.runtime,
			 org.eclipse.core.resources,
			 org.eclipse.sirius,
			 org.eclipse.sirius.common.acceleo.aql,
			 org.eclipse.sirius.diagram,
			 «FOR Map.Entry<String, List<Service>> service: recommenderToServices.entrySet»
			 	«FOR serv: service.value»
			 		«FOR Recommender rec: serv.services»
			 			«IF !(rec.details.targetEClass instanceof EClass)»
			 «GenModelUtils.getProjectFromEClass(rec.details.targetEClass)»				
			 			«ENDIF»
			 		«ENDFOR»
			 	«ENDFOR»
			 «ENDFOR»
			 org.eclipse.emf,
			 integrate.recommenders.ironman.definition,
			 integrate.recommenders.ironman.wizard,
			 com.fasterxml.jackson.core.jackson-core,
			 com.fasterxml.jackson.core.jackson-databind
		'''	
	}
	
	override automaticModuleName() {
		'''
			Automatic-Module-Name: «this.project.name»
		'''
	}
	
	override requiredExecutionEnvironment() {
		'''
			Bundle-RequiredExecutionEnvironment: JavaSE-17
		'''
	}	
}
