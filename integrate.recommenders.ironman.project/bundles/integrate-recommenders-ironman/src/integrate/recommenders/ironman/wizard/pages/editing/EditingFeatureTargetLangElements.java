package integrate.recommenders.ironman.wizard.pages.editing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.mapping.MapItemElement;

public class EditingFeatureTargetLangElements extends EditingSupport {
	
	private EList<EAttribute> listOfAttributes;
	
	public EditingFeatureTargetLangElements(ColumnViewer viewer) {
		super(viewer);
		this.listOfAttributes = new BasicEList<EAttribute>();
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		final MapItemElement mapItemElement = (MapItemElement) element;	
		final List<String> listFeatureElements = new ArrayList<String>();
		if (mapItemElement.getTypeEClass() != null) {
			updateListOfFeatures(mapItemElement.getTypeEClass(), listFeatureElements);
		}		
		return new ComboBoxCellEditor((Composite)getViewer().getControl(), listFeatureElements.toArray(new String[0]),
				SWT.BORDER 
				| SWT.READ_ONLY				
				);
	}

	private void updateListOfFeatures(EClass eClass, List<String> listFeatureElements) {
		this.listOfAttributes.clear();
		eClass.getEAllAttributes().stream().collect(Collectors.toCollection(() -> this.listOfAttributes));
		this.listOfAttributes.stream()
			.map(feat -> feat.getName())
			.collect(Collectors.toCollection(() -> listFeatureElements));		
	}

	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof MapItemElement)
			return true;
		return false;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof MapItemElement) {
			final MapItemElement mapItemElement = (MapItemElement) element;
			return this.listOfAttributes.indexOf(mapItemElement.getFeature());
		}		
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof MapItemElement && value instanceof Integer && (Integer) value != -1) {
			final MapItemElement mapItemElement = (MapItemElement) element;
			mapItemElement.setFeature(this.listOfAttributes.get((Integer)value));
			getViewer().update(element, null);
		}
	}

}
