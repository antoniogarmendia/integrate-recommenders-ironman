package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;

public class TargetFeatureLanguageProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		final Object element = cell.getElement();
		if (element instanceof MapItemElement) {
			final MapItemElement mapItemElement = (MapItemElement) element;
			if (mapItemElement.getFeature() != null) 
				cell.setText(mapItemElement.getFeature().getName());
		}
	}

}
