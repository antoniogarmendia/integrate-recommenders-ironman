package integrate.recommenders.ironman.generate.sirius.generator.template

import org.eclipse.core.resources.IProject
import project.generator.api.template.MetaInfTemplate
import java.util.Map
import java.util.List
import integrate.recommenders.ironman.definition.services.Service
import org.eclipse.emf.ecore.EClass
import project.generator.api.utils.GenModelUtils
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.BasicEList

class MetaInfRecommender extends MetaInfTemplate {
	
	val IProject project;
	val Map<String, List<Service>> recommenderToServices;
		
	new(IProject project, Map<String, List<Service>> recommenderToServices) {
		this.project = project;		
		this.recommenderToServices = recommenderToServices;
	}
	
	override bundleNameSymbolic() {
		'''
			Bundle-Name: %pluginName
			Bundle-SymbolicName: «this.project.name»;singleton:=true
			Bundle-Activator: «this.project.name».Activator
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
			 «FOR eClass : listOfDifferentTarget»
			 «GenModelUtils.getProjectFromEClass(eClass)»,				
			 «ENDFOR»
			 org.eclipse.emf,
			 integrate.recommenders.ironman.definition,
			 integrate.recommenders.ironman.wizard,
			 com.fasterxml.jackson.core.jackson-core,
			 com.fasterxml.jackson.core.jackson-databind
		'''	
	}
	
	def EList<EClass> listOfDifferentTarget() {
		val listOfClasses = new BasicEList<EClass>();
		for(Map.Entry<String, List<Service>> service: recommenderToServices.entrySet){
			for (serv: service.value){
				if (!serv.detail.obtainTargetEClass.name.equals("EClass")
					&& !listOfClasses.contains(serv.detail.obtainTargetEClass)
				){
					listOfClasses.add(serv.detail.obtainTargetEClass);
				}
			}			
		}
		return listOfClasses;
	}
	
	override automaticModuleName() {
		'''
			Automatic-Module-Name: «this.project.name»
		'''
	}
	
	override requiredExecutionEnvironment() {
		'''
			Bundle-ActivationPolicy: lazy
			Bundle-RequiredExecutionEnvironment: JavaSE-17
		'''
	}	
}
