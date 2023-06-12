package project.generator.api.template.sirius

import org.eclipse.core.resources.IProject
import java.util.Set
import project.generator.api.template.AbstractPluginXMLTemplate

class ViewpointEditorPluginXML extends AbstractPluginXMLTemplate {
	
	val IProject project;
	val Set<String> items;
	val String packageName;
	
	new (IProject project, String packageName, Set<String> items){
		this.project = project;
		this.items = items;
		this.packageName = packageName;
	}
	
	override middle() {
		'''
			<extension point="org.eclipse.sirius.componentization">
			    <component class="«project.name».Activator"
			               id="«project.name»"
			        name="«project.name»">
			    </component>
			</extension>
			<extension
			        point="org.eclipse.sirius.externalJavaAction">
			        «FOR item: items»
			        	<javaActions
			        		 actionClass="«packageName».Recommend«item»"
			        		 id="«packageName».«item»">
			        	</javaActions>
			        «ENDFOR»		    
			  </extension>
		'''
	}
	
}
