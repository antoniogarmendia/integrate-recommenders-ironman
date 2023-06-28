package integrate.recommenders.ironman.generate.tree.editor.template

import project.generator.api.template.AbstractPluginXMLTemplate

class TreeEditorPluginXML extends AbstractPluginXMLTemplate {
	
	val String packageName;
	
	new(String packageName){
		this.packageName = packageName;
	}
	
	override middle() {
		'''
		<extension
		         point="org.eclipse.ui.menus">
		      <menuContribution
		            allPopups="true"
		            class="«this.packageName».RecommenderPopup"
		            locationURI="popup:org.eclipse.ui.popup.any?after=additions">
		      </menuContribution>
		   </extension>
		'''
	}
	
}