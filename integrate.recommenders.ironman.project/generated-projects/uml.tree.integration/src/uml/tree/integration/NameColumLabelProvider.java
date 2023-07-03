package uml.tree.integration;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class NameColumLabelProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		final Object element = cell.getElement();
		if (element instanceof RecommenderData) {
			cell.setText(((RecommenderData) element).getName());
		}
	}
}		
