package integrate.recommenders.ironman.wizard.pages.contents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITreeContentProvider;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.TargetItemElement;

public class MLConfigureLanguageContentProvider implements ITreeContentProvider {
	
	@Override 
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof MLMappingConfiguration) {
			final MLMappingConfiguration mapConfig = (MLMappingConfiguration) inputElement;
			if (mapConfig.getMapTargetElementToTargetItems() != null)
				return mapConfig.getMapTargetElementToTargetItems().entrySet().toArray();			
			}
		return Collections.EMPTY_LIST.toArray();
	}

	@SuppressWarnings("serial")
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Entry) {
			final Entry<?,?> entry = (Entry<?, ?>) parentElement;
			if (entry.getValue() instanceof List) {
				return ((List<?>)entry.getValue()).toArray();
			}
		}		
		if (parentElement instanceof TargetItemElement) {
			final TargetItemElement targetItemElement = (TargetItemElement) parentElement;
			return new ArrayList<Object>(){ 
				{
					add(targetItemElement.getRead());
					add(targetItemElement.getWrite());
					add(targetItemElement.getFeature());
				}
			}.toArray();			
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
		if (element instanceof Entry) {
			final Entry<?,?> entry = (Entry<?, ?>) element;
			if (entry.getValue() instanceof List) {
				final List<?> listOfElements = (List<?>)entry.getValue();
				return (listOfElements.size() != 0)? true : false;
			}
		} else if (element instanceof TargetItemElement) {
			return true;
		}
		return false;
	}
}
