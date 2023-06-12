package project.generator.api.template

abstract class AbstractJavaClass {
	
	val String className;
	val String packageName;
	
	new(String className, String packageName) {
		this.className = className;
		this.packageName = packageName;
	}
	
	def getClassName() {
		this.className;
	}
	
	def getPackageName() {
		this.packageName;
	}
	
}
