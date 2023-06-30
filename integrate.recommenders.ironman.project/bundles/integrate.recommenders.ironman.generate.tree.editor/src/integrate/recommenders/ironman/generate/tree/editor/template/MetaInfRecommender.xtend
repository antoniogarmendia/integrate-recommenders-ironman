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
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration
import integrate.recommenders.ironman.definition.mapping.TargetElement
import integrate.recommenders.ironman.definition.mapping.TargetItemElement

class MetaInfRecommender extends MetaInfTemplate {
	
	val IProject project;
	val Map<String, List<Service>> recommenderToServices;
	val MLMappingConfiguration mapping;
	
	new(IProject project, Map<String, List<Service>> recommenderToServices, MLMappingConfiguration mapping) {
		this.project = project;	
		this.recommenderToServices = recommenderToServices;
		this.mapping = mapping;
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
		 «FOR EClass target: listOfDifferentTarget»
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
	
	def EList<EClass> listOfDifferentTarget() {
		if (mapping === null) {
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
		} else {
			return listOfDifferentTargetMapping;
		}	
	}
	
	def EList<EClass> listOfDifferentTargetMapping() {
		val listOfClasses = new BasicEList<EClass>();
		for (Map.Entry<TargetElement,List<TargetItemElement>> entry: mapping.mapTargetElementToTargetItems.entrySet) {
			val TargetElement targetElement = entry.key;
			listOfClasses.add(targetElement.targetElement);
		}		
		return listOfClasses;
	}
}