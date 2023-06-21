package integrate.recommenders.ironman.wizard.pages.contents;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import integrate.recommenders.ironman.definition.services.Service;

public class SelectItemContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List)
			return ((List<?>) inputElement).toArray();
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Service)
			return ((Service) parentElement).getDetail().getItems().toArray();
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Service)
			return true;		
		return false;
	}
	
}
