package integrate.recommenders.ironman.generate.sirius.generator.template

import org.eclipse.core.resources.IProject
import project.generator.api.template.MetaInfTemplate

class MetaInfRecommender extends MetaInfTemplate {
	
	val IProject project;
		
	new(IProject project) {
		this.project = project;		
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
			 org.eclipse.emf
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
