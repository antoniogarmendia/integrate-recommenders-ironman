package project.generator.api.template

abstract class AbstractPluginXMLTemplate implements IGeneration{
	
	def begin(){
		'''
			<?xml version="1.0" encoding="UTF-8"?>
			<?eclipse version="3.4"?>
			<plugin>
		'''		
	}
	
	def String middle()
	
	override doGenerate() {
		val StringBuilder content = new StringBuilder();
		content.append(begin());
		content.append(middle());
		content.append(end());
		return content.toString		
	}
	
	def end(){
		'''
			</plugin>
		'''
	}
}
