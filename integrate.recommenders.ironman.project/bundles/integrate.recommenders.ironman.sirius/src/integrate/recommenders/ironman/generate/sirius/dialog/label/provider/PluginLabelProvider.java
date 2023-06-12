package integrate.recommenders.ironman.generate.sirius.dialog.label.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import static integrate.recommenders.ironman.generate.sirius.dialog.utils.RecommenderViewpointUtils.*;

public class PluginLabelProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Viewpoint)
			cell.setText(getPluginNameFromViewpoint((Viewpoint)element));		
	}

}
