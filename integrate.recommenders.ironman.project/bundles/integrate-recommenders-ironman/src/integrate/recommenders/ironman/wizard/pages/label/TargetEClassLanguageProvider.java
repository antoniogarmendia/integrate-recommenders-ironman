package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;

public class TargetEClassLanguageProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		final Object element = cell.getElement();
		if (element instanceof MapItemElement) {
			final MapItemElement mapItemElement = (MapItemElement) element;
			if (mapItemElement.getTypeEClass() != null) 
				cell.setText(mapItemElement.getTypeEClass().getName());
		}
	}

}
