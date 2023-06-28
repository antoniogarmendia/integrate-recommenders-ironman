package integrate.recommenders.ironman.generate.tree.editor.template.dialog

import project.generator.api.template.IGeneration

class RecommenderData implements IGeneration {
	
	val String packageName;
	
	new(String packageName) {
		this.packageName = packageName;
	}
	
	override doGenerate() {
		'''
			package «this.packageName»;
			
			public class RecommenderData {
				
				private String name;
				private String recommenders;
				private String rating;
				
				public RecommenderData(String name, String recommenders, String rating) {
					this.name = name;
					this.recommenders = recommenders;
					this.rating = rating;
				}
				
				public String getName() {
					return name;
				}
				
				public String getRecommenders() {
					return recommenders;
				}
				
				public String getRating() {
					return rating;
				}
			}		
		'''
	}
	
	
}
