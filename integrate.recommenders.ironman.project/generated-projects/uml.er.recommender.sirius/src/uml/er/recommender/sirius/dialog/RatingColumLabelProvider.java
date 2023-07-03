package uml.er.recommender.sirius.dialog;

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
