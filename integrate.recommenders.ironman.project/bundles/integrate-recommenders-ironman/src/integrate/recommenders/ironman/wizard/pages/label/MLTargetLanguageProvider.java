package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;
import integrate.recommenders.ironman.definition.mapping.MapTargetElement;

public class MLTargetLanguageProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			final EClass eClass = ((MapTargetElement)entry.getKey()).getTargetElement();
			if (eClass != null)			
				cell.setText(eClass.getName());			
		} else if (element instanceof MapItemElement) {
			final ENamedElement structFeat = ((MapItemElement) element).getWriteElement();
			if (structFeat != null)
				cell.setText(structFeat.getName());
		}
	}

}
