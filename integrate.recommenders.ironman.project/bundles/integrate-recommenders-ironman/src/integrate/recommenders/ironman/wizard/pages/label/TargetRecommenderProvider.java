package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Service;

public class TargetRecommenderProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Service)
			cell.setText(((Service) element).getDetail().getTarget());
	}

}
