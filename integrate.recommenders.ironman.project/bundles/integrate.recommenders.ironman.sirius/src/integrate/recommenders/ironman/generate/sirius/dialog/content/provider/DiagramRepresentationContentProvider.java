package integrate.recommenders.ironman.generate.sirius.dialog.content.provider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

public class DiagramRepresentationContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Viewpoint) {
			return ((Viewpoint) parentElement).getOwnedRepresentations().toArray(new Object[0]);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// Do nothing
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Viewpoint)
			return true;
		return false;
	}

}
