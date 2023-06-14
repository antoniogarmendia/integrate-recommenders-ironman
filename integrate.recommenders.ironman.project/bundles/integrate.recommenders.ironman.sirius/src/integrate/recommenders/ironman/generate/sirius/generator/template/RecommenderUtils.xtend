package integrate.recommenders.ironman.generate.sirius.generator.template

import project.generator.api.template.IGeneration
import java.util.Map
import java.util.List
import integrate.recommenders.ironman.definition.services.Service

class RecommenderUtils implements IGeneration {
	
	val String packageName;
	val Map<String, List<Service>> recommenderToServices;
	
	new(String packageName, Map<String, List<Service>> recommenderToServices) {
		this.packageName = packageName;
		this.recommenderToServices = recommenderToServices;
	}
	
	override doGenerate() {
		'''
			package «packageName»;
			
			public final class RecommenderUtils {
				
				private RecommenderUtils() {
					throw new AssertionError("This class does not allow inheritance");
				}
				//URLs of the Recommenders
				«recommenderURLs»
			}			
		'''
	}
	
	def recommenderURLs() {
		'''
		«var i = 1»
		«FOR Map.Entry<String, List<Service>> entryRecommend : recommenderToServices.entrySet»
			public static final String URL_RECOMMENDER_«i++» = "«entryRecommend.key»";
			«FOR services: entryRecommend.value»
				«FOR service: services.services»
				public static final String RECOMMENDER_«i»_«service.name» = "«service.name»";
				«ENDFOR»
			«ENDFOR»			
		«ENDFOR»
		'''
	}
	
}