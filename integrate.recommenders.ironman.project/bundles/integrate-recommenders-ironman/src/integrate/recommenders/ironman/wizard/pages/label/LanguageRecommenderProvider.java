package integrate.recommenders.ironman.wizard.pages.label;

import java.util.Map;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;

public class LanguageRecommenderProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Map.Entry) {
			cell.setText(((Map.Entry<?, ?>) element).getKey().toString());			
		}
		
		if (element instanceof Service)
			cell.setText(((Service) element).getName());
		//TODO update
//		if (element instanceof Recommender)
//			cell.setText(((Recommender) element).getName());
	}

}
