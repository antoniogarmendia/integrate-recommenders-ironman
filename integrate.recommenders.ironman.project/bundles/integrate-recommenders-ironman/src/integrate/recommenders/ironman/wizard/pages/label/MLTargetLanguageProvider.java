package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import integrate.recommenders.ironman.definition.mapping.AbstractItemElement;
import integrate.recommenders.ironman.definition.mapping.TargetElement;
import integrate.recommenders.ironman.definition.mapping.TargetItemElement;

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
		} else if (element instanceof TargetItemElement) {
			final TargetItemElement targetElement = (TargetItemElement) element;
			if (targetElement.getRead().getStructFeature() != null)
				cell.setText("Item: " + targetElement.getRead().getStructFeature().getEType().getName());
		}	
		//Remove the bold in font
		cell.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));
	}

}
