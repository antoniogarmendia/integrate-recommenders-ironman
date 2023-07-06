package integrate.recommenders.ironman.generate.sirius.dialog;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.widgets.LabelFactory;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import integrate.recommenders.ironman.generate.sirius.dialog.content.provider.DiagramRepresentationContentProvider;
import integrate.recommenders.ironman.generate.sirius.dialog.label.provider.DiagramRepresentationLabelProvider;
import integrate.recommenders.ironman.generate.sirius.dialog.label.provider.PluginLabelProvider;
import static integrate.recommenders.ironman.generate.sirius.dialog.utils.DesignGeneratorUtils.*;

import static integrate.recommenders.ironman.generate.sirius.dialog.utils.RecommenderViewpointUtils.*;

public class SiriusViewpointDialog extends Dialog {

	private final String metaModel;
	private String projectName;
	private CheckboxTreeViewer checkboxTreeViewer;
	private EList<DiagramDescription> selectedDiagramDescriptions;
	
	public SiriusViewpointDialog(Shell parentShell, String metaModel) {
		super(parentShell);		
		this.metaModel = metaModel;
		this.selectedDiagramDescriptions = new BasicEList<DiagramDescription>();
		this.projectName = "recommender.sirius";
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	// Set the title of the dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Viewpoint Selection Dialog");
	}
	
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if (label.equals(IDialogConstants.OK_LABEL)) {
			button.setEnabled(false);
		}
		return button;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(GridLayoutFactory.fillDefaults().create());		
		//container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL,GridData.FILL_VERTICAL));
		LabelFactory.newLabel(SWT.NONE).text("Select Available Representations")
			.create(container)
			//.setLayoutData(new GridData(GridData.FILL,GridData.FILL, true, true));
			;
		
		//Text Project Name
		WidgetFactory.label(SWT.NONE).text("Project Name: ").create(container);
				
		WidgetFactory.text(SWT.NONE).text(this.projectName)
			.onModify(t -> {
					projectName = ((Text)t.widget).getText();
			})
			.create(container)
			.setLayoutData(new GridData(GridData.FILL,GridData.FILL, false, false));
			;
		
		//Scrolled Composite
		final ScrolledComposite sc = new ScrolledComposite(container, SWT.V_SCROLL);
		sc.setLayoutData(GridDataFactory.fillDefaults().create());
		
		createTreeViewer(sc);
		
		return super.createDialogArea(parent);
	}
	
	private void createTreeViewer(ScrolledComposite sc) {		
		checkboxTreeViewer = new CheckboxTreeViewer(sc, SWT.VIRTUAL | SWT.BORDER | SWT.CHECK ); 
		checkboxTreeViewer.getTree().setHeaderVisible(true);
		checkboxTreeViewer.getTree().setLinesVisible(true);
		checkboxTreeViewer.setUseHashlookup(true);
		
		checkboxTreeViewer.getTree().setLayoutData(GridDataFactory.fillDefaults().create());
		
		//Diagram Representation Column
		TreeViewerColumn diagramRepreCol = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		diagramRepreCol.getColumn().setWidth(180);
		diagramRepreCol.getColumn().setText("Diagram Representation");
		
		//Diagram Representation Column
		TreeViewerColumn pluginCol = new TreeViewerColumn(checkboxTreeViewer, SWT.LEFT);
		pluginCol.getColumn().setWidth(180);
		pluginCol.getColumn().setText("Plugin Name");
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setAlwaysShowScrollBars(true);
		
		sc.setContent(checkboxTreeViewer.getTree());		
		
		//Provider Diagram Description Column
		diagramRepreCol.setLabelProvider(new DiagramRepresentationLabelProvider());		
		//Provider Diagram Description Column
		pluginCol.setLabelProvider(new PluginLabelProvider());			
		
		checkboxTreeViewer.setContentProvider(new DiagramRepresentationContentProvider());	
		checkboxTreeViewer.setInput(getAllViewpointbyMetamodelNsURI(this.metaModel));
		
		treeViewerStyle(checkboxTreeViewer);
		
		addListener(checkboxTreeViewer);
	}	
	
	private void addListener(CheckboxTreeViewer checkboxTreeViewer) {
		checkboxTreeViewer.getTree().addListener(SWT.Selection, event ->{
			 if (event.detail == SWT.CHECK) {
				 TreeItem item = (TreeItem) event.item;
				 Object object = item.getData();
				 boolean checked = item.getChecked();
				 if (object instanceof DiagramDescription) {
					 if (checked == true) {
						 checkParent(item);
						 getButton(IDialogConstants.OK_ID).setEnabled(true); 
					 } else {
						 uncheckParent(item); 
						 checkPageComplete(checkboxTreeViewer);
					 }
				 } else {
					 item.setChecked(false);
					 uncheckedAllChildren(item);
				 }
			 }
		});		
	}

	private void checkPageComplete(CheckboxTreeViewer checkboxTreeViewer) {
		if (checkboxTreeViewer.getCheckedElements().length == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false); 
		} 			
	}

	private void uncheckParent(TreeItem item) {
		TreeItem parent = item.getParentItem();
		if(parent!=null) {
			if(parent.getChecked() == true) {
				parent.setChecked(false);	
			}
		}		
	}

	private void checkParent(TreeItem item) {
		TreeItem parent = item.getParentItem();
		if(parent!=null) {
			if(parent.getChecked() == false) {
				parent.setChecked(true);				
			}
		}		
	}

	private void uncheckedAllChildren(TreeItem item) {
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			TreeItem treeItem = items[i];
			treeItem.setChecked(false);			
		}		
	}
	
	@Override
	protected void okPressed() {
		Object[] checkedElements = this.checkboxTreeViewer.getCheckedElements();
		for (Object checkedElement : checkedElements) {
			if (checkedElement instanceof DiagramDescription)
				this.selectedDiagramDescriptions.add((DiagramDescription) checkedElement);
		}
		super.okPressed();
	}
	
	public EList<DiagramDescription> getSelectedDiagramDescriptions() {
		return selectedDiagramDescriptions;
	}
	
	public String getProjectName() {
		return projectName;
	}
}
