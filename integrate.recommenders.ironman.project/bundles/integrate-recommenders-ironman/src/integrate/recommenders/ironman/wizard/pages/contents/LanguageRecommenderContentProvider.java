package integrate.recommenders.ironman.wizard.pages.contents;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;

import integrate.recommenders.ironman.definition.services.Service;

public class LanguageRecommenderContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Map)
			return ((Map<?,?>)inputElement).entrySet().toArray();
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Map.Entry
				&& ((Map.Entry<?, ?>) parentElement).getValue() instanceof List) {
			return ((List<?>)((Map.Entry<?, ?>) parentElement).getValue()).toArray();
		}
		if (parentElement instanceof Service)
			return ((Service) parentElement).getServices().toArray();
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Map.Entry)
			return true;
		if (element instanceof Service)
			return true;
		return false;
	}

}
