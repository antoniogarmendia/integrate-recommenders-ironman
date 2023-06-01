package integrate.recommenders.ironman.wizard.preferences;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public class ServerContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof String) {
			final String strElement = (String) inputElement;
			return strElement.split("\\|");
		}
		return null;
	}

}
