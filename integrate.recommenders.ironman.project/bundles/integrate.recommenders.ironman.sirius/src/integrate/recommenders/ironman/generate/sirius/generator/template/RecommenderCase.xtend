package integrate.recommenders.ironman.generate.sirius.generator.template

import project.generator.api.template.IGeneration

class RecommenderCase implements IGeneration{
	
	val String packageName;
	
	new(String packageName) {
		this.packageName = packageName;
	}
	
	override doGenerate() {
		'''
		package «this.packageName»;
		
		import java.util.Collection;
		import java.util.Map;
		
		public class RecommenderCase {
			
			private Map<String, Collection<String>> urlToRecommenders;
			private String type;
			private String targetName;
			
			public RecommenderCase(Map<String, Collection<String>> urlToRecommenders, String type, String targetName) {
				this.urlToRecommenders = urlToRecommenders;
				this.type = type;
				this.targetName = targetName;
			}
		
			public String getType() {
				return type;
			}
			
			public Map<String, Collection<String>> getUrlToRecommenders() {
				return urlToRecommenders;
			}	
			
			public String getTargetName() {
				return targetName;
			}
		}
		'''
	}
	
}