package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Recommender;

public class ItemRecommenderProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Recommender) {
			final String cellText = String.join(",", ((Recommender) element).getDetails().getItems());
			cell.setText(cellText);
		}
	}

}
