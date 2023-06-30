package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.mapping.AbstractItemElement;
import integrate.recommenders.ironman.definition.mapping.ActualFeature;
import integrate.recommenders.ironman.definition.mapping.ReadFeature;
import integrate.recommenders.ironman.definition.mapping.TargetElement;
import integrate.recommenders.ironman.definition.mapping.TargetItemElement;
import integrate.recommenders.ironman.definition.mapping.WriteFeature;

public class MLSourceLanguageProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		final Object element = cell.getElement();
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			if (entry.getKey() instanceof TargetElement)
				cell.setText(((TargetElement)entry.getKey()).getSourceElement());			
		} else if (element instanceof TargetItemElement) {
			cell.setText("Item: " + ((TargetItemElement)element).getClassName());		
		} else if (element instanceof ReadFeature) {
			cell.setText("Read: " + ((AbstractItemElement)element).getItem());
		} else if (element instanceof WriteFeature) {
			cell.setText("Write: " + ((AbstractItemElement)element).getItem());
		}  else if (element instanceof ActualFeature) {
			cell.setText("Feature: " + ((AbstractItemElement)element).getItem());
		}		
	}

}
