package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Recommender;

public class SelectItemRecommenderProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Recommender) {
			cell.setText(((Recommender) element).getName());
		}
		if (element instanceof Map.Entry) {
			cell.setText((String)((Map.Entry<?, ?>) element).getKey());
		}
		if (element instanceof String)
			cell.setText((String) element);
		System.out.println(element.toString());
	}

}
