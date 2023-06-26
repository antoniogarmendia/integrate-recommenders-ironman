package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.AbstractItemElement;
import integrate.recommenders.ironman.definition.mapping.TargetElement;

public class MLTargetLanguageProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			final EClass eClass = ((TargetElement)entry.getKey()).getTargetElement();
			if (eClass != null)			
				cell.setText(eClass.getName());			
		} else if (element instanceof AbstractItemElement) {
			final AbstractItemElement abstractItemElement = ((AbstractItemElement) element);
			if (abstractItemElement.getStructFeature() != null)
				cell.setText(abstractItemElement.getStructFeature().getName());
		}		
	}

}
