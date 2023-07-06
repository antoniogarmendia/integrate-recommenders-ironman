package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Service;

public class LanguageRecommenderProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Map.Entry) {
			cell.setText(((Map.Entry<?, ?>) element).getKey().toString());			
		}
		else if (element instanceof Service)
			cell.setText(((Service) element).getName());
		//Remove the bold in font
		cell.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));
	}

}
