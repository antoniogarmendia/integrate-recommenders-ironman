package integrate.recommenders.ironman.generate.tree.editor.template.dialog

import project.generator.api.template.IGeneration

class RatingColumLabelProvider implements IGeneration {
	
	val String packageName;
	
	new(String packageName) {
		this.packageName = packageName;
	}
	
	override doGenerate() {
		'''
			package «this.packageName»;
			
			import org.eclipse.jface.viewers.CellLabelProvider;
			import org.eclipse.jface.viewers.ViewerCell;
			
			public class RatingColumLabelProvider extends CellLabelProvider {
			
				@Override
				public void update(ViewerCell cell) {
					final Object element = cell.getElement();
					if (element instanceof RecommenderData) {
						cell.setText(((RecommenderData) element).getRating());
					}
				}
			}
		'''
	}	
}
