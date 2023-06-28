package integrate.recommenders.ironman.generate.tree.editor.template

import org.eclipse.core.resources.IProject
import project.generator.api.template.MetaInfTemplate
import java.util.List
import integrate.recommenders.ironman.definition.services.Service
import java.util.Map
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EClass
import static project.generator.api.utils.GenModelUtils.*
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
		 «FOR EClass target: listOfTargets»
		 «getProjectFromEClass(target) + ","»
		 «ENDFOR»
		 org.eclipse.core.expressions;bundle-version="3.8.100",
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
		Bundle-RequiredExecutionEnvironment: JavaSE-11
		'''
	}	
	
	def EList<EClass> listOfTargets(){
		var listOfTargets = new BasicEList<EClass>();
		for (Map.Entry<String, List<Service>> entryService: recommenderToServices.entrySet) {
			for (Service service: entryService.value) {
				if (!listOfTargets.contains(service.detail.obtainTargetEClass))
					listOfTargets.add(service.detail.obtainTargetEClass);
			}
		}		
		return listOfTargets;
	}
}