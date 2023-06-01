package integrate.recommenders.ironman.wizard.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import integrate.recommenders.ironman.wizard.Activator;

public class IronManPreferenceInitializer extends AbstractPreferenceInitializer {

	public IronManPreferenceInitializer() {
		// Do nothing
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(IronManPreferenceConstants.IRONMAN_LIST_SERVERS, "http://dimo1.ii.uam.es/IronManAPI/");
	}

}
