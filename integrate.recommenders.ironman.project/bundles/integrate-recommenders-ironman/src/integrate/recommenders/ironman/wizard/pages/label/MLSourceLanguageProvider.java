package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;
import integrate.recommenders.ironman.definition.mapping.MapTargetElement;

public class MLSourceLanguageProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			if (entry.getKey() instanceof MapTargetElement)
				cell.setText(((MapTargetElement)entry.getKey()).getSourceElement());			
		} else if (element instanceof MapItemElement) {
			cell.setText(((MapItemElement)element).getItem());		
		}
	}

}
