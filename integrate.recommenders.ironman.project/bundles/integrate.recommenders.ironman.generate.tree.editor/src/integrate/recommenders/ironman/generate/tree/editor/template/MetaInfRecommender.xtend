package integrate.recommenders.ironman.generate.tree.editor.template

import org.eclipse.core.resources.IProject
import project.generator.api.template.MetaInfTemplate

class MetaInfRecommender extends MetaInfTemplate {
	
	val IProject project;
	//val RecommenderConfiguration recommenderCofig;
	
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
«««		 «getProjectFromEClass(recommenderCofig.target.klass) + ","»
		 org.eclipse.core.expressions;bundle-version="3.8.100"		 
		'''
	}
	
	override automaticModuleName() {
		'''
		Automatic-Module-Name: «this.project.name»
		'''
	}
	
	override requiredExecutionEnvironment() {
		'''
		Bundle-RequiredExecutionEnvironment: JavaSE-11
		'''
	}	
}