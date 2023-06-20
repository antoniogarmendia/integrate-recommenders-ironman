package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Collection;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Service;

public class ItemRecommenderProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Service) {
			final Collection<String> items = ((Service) element)
												.getDetail().getItems().stream()
												.map(item -> item.getRead())
												.toList();		
			final String cellText = String.join(",", items);
			cell.setText(cellText);
		}
	}
}
