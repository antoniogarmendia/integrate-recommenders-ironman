package integrate.recommenders.ironman.generate.tree.editor.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TreeEditorDialog extends Dialog {

	public String projectName;
	
	public TreeEditorDialog(Shell parentShell, String projectName) {
		super(parentShell);	
		this.projectName = projectName;
	}
	
	// Set the title of the dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Create Tree Editor Recommendation Project");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		
		//Text Project Name
		WidgetFactory.label(SWT.NONE).text("Project Name: ").create(container);
		
		WidgetFactory.text(SWT.NONE).text(this.projectName)
				.onModify(t -> {
						projectName = ((Text)t.widget).getText();
				})
				.create(container);
		
		return super.createDialogArea(parent);
	}
	
	public String getProjectName() {
		return projectName;
	}

}
