package integrate.recommenders.ironman.wizard.pages.label;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;

public class MLWriteFeatureProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof MapItemElement) {
			final ENamedElement structFeat = ((MapItemElement) element).getWriteElement();
			if (structFeat != null)
				cell.setText(structFeat.getName());
		}
	}

}
