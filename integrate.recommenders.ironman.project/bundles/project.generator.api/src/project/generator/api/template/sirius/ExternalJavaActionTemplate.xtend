package project.generator.api.template.sirius

import project.generator.api.template.IGeneration
import project.generator.api.template.AbstractJavaClass

abstract class ExternalJavaActionTemplate extends AbstractJavaClass implements IGeneration {
	
	new(String className, String packageName) {
		super(className, packageName)
	}
	
	override doGenerate() {
		'''
			«importDependencies»
			«start»
				«startConstructor»
				«middleConstructor»
				«endConstructor»
				«startCanExecute»
				«middleDefaultCanExecute»
				«endCanExecute»
				«startExecute»
				«middleDefaultExecute»
				«endExecute»
			«end»		
		'''
	}
		
	def importDependencies() {
		'''
			package «packageName»;
			
			import java.util.Collection;
			import java.util.Map;
			
			import org.eclipse.emf.ecore.EObject;
			import org.eclipse.sirius.diagram.DNodeList;
			import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;		
		'''
	}
	
	def start() {
		'''
			
			public class «className» implements IExternalJavaAction {
		'''
	}
	
	def end() {
		'''}'''
	}
	
	def startConstructor() {
		'''
			public «className»() {
		'''
	}
	
	def middleConstructor() {
		//Do nothing
	}
	
	def endConstructor() {
		'''}'''
	}
	
	def startCanExecute() {
		'''
			@Override
			public boolean canExecute(Collection<? extends EObject> arg0) {
		'''	
	}
	
	def middleDefaultCanExecute() {
		'''
			return true;
		'''
	}
	
	def endCanExecute() {
		'''}'''
	}
	
	def startExecute() {
		'''
			@Override
			public void execute(Collection<? extends EObject> selectedElements, Map<String, Object> parameters) {
		'''
	}
	
	def middleDefaultExecute() {
		'''
			// TODO Auto-generated method stub
		'''
	}
	
	def endExecute() {
		'''
			}
		'''
	}	
	
}
