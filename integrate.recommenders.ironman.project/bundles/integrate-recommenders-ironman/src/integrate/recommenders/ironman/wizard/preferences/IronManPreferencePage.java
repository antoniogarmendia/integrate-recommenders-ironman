package integrate.recommenders.ironman.wizard.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import integrate.recommenders.ironman.wizard.Activator;

public class IronManPreferencePage 
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public IronManPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preference Page of the IronMan plugin");
	}
	
	public void createFieldEditors() {
		addField(
				new ListTableEditor(IronManPreferenceConstants.IRONMAN_LIST_SERVERS, 
						getFieldEditorParent()));
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}
