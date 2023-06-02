package integrate.recommenders.ironman.wizard.pages.contents;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;

import integrate.recommenders.ironman.definition.services.Recommender;

public class SelectItemContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Map)
			return ((Map<?,?>)inputElement).entrySet().toArray();
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Recommender)
			return ((Recommender) parentElement).getDetails().getItems().toArray();
		if (parentElement instanceof Map.Entry) {
			final Object value = ((Map.Entry<?,?>)(parentElement)).getValue();
			return ((List<?>) value).toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Recommender)
			return true;
		if (element instanceof Map.Entry) 
			return true;
		return false;
	}

	
}
