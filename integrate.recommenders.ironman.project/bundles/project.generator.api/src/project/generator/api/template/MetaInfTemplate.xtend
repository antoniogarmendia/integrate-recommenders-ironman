package project.generator.api.template

abstract class MetaInfTemplate implements IGeneration {
	
	def begin(){
		'''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			«bundleNameSymbolic»
			Bundle-Version: 1.0.0.qualifier
		'''		
	}

	def String bundleNameSymbolic()
	def String exportPackage() 
	def String requireBundle();
	def String automaticModuleName();
	def String requiredExecutionEnvironment();
	
	override doGenerate() {
		val StringBuilder content = new StringBuilder();
		content.append(begin());
		content.append(exportPackage());
		content.append(requireBundle());
		content.append(automaticModuleName());
		content.append(requiredExecutionEnvironment());
		return content.toString
	}	
	
}
