package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map.Entry;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

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
			styleRangeCell(6,cell);
		} else if (element instanceof ReadFeature) {
			cell.setText("Read: " + ((AbstractItemElement)element).getItem());
			styleRangeCell(6,cell);
		} else if (element instanceof WriteFeature) {
			cell.setText("Write: " + ((AbstractItemElement)element).getItem());
			styleRangeCell(7,cell);
		}  else if (element instanceof ActualFeature) {
			cell.setText("Feature: " + ((AbstractItemElement)element).getItem());
			styleRangeCell(9,cell);
		}		
		//Remove the bold in font
		cell.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));		
	}

	private void styleRangeCell(int index, ViewerCell cell) {		
		final StyleRange italicRange = new StyleRange(0, index, null,
                null, SWT.ITALIC);
		final StyleRange[] range = { italicRange };
		cell.setStyleRanges(range);
	}

}
