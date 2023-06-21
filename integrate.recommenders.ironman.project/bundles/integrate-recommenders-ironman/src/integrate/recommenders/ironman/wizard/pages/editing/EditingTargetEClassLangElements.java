package integrate.recommenders.ironman.wizard.pages.editing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.MapItemElement;

public class EditingTargetEClassLangElements extends EditingSupport {

	private final MLMappingConfiguration mapping;
	private EList<EClass> listOfEClasses;
	
	public EditingTargetEClassLangElements(ColumnViewer viewer, MLMappingConfiguration mapping) {
		super(viewer);	
		this.mapping = mapping;
		this.listOfEClasses = new BasicEList<EClass>();
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		final EPackage ePackage = mapping.getEPackage();
		final List<String> listOfCCElements = new ArrayList<String>();
		fillListOfClassesCC(ePackage, listOfCCElements);
		
		return new ComboBoxCellEditor((Composite)getViewer().getControl(), listOfCCElements.toArray(new String[0]),
				SWT.BORDER 
				| SWT.READ_ONLY				
				);
	}

	private void fillListOfClassesCC(EPackage ePackage, List<String> listOfCCElements) {
		if (ePackage != null) {
			this.listOfEClasses.clear();
			ePackage.getEClassifiers()
				.stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.collect(Collectors.toCollection(() -> listOfEClasses));
			
			listOfEClasses.stream()
					.map(eClass -> eClass.getName())
					.collect(Collectors.toCollection(() -> listOfCCElements));		
		}		
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
			return this.listOfEClasses.indexOf(mapItemElement.getTypeEClass());
		}
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof MapItemElement && value instanceof Integer && (Integer) value != -1) {
			final MapItemElement mapItemElement = (MapItemElement) element;
			mapItemElement.setTypeEClass(this.listOfEClasses.get((Integer)value));
			getViewer().update(element, null);
		}
	}

}
