package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Item;
import integrate.recommenders.ironman.definition.services.Service;

public class SelectItemRecommenderProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Service) {
			cell.setText(((Service) element).getName());
		}
		if (element instanceof Item)
			cell.setText(((Item) element).getRead());
		//Remove the bold in font
		cell.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));	
	}

}
