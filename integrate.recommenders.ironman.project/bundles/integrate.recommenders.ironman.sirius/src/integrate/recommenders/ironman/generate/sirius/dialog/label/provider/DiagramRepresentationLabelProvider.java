package integrate.recommenders.ironman.generate.sirius.dialog.label.provider;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.ui.tools.internal.viewpoint.ViewpointHelper;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;

@SuppressWarnings("restriction")
public class DiagramRepresentationLabelProvider extends StyledCellLabelProvider {
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof Viewpoint) {
			cell.setText(((Viewpoint) element).getName());			
			cell.setImage(ViewpointHelper.getImage((Viewpoint)element));
		}
		else if (element instanceof DiagramDescription) {
			cell.setText(((DiagramDescription) element).getName());
			cell.setImage(SiriusEditPlugin.getPlugin().getImage(SiriusEditPlugin.getPlugin().getItemImageDescriptor(element)));
		}
		//Remove the bold in font
		cell.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));
	}

}
