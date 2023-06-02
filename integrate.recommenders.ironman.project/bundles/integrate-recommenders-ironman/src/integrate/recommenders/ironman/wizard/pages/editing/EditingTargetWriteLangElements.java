package integrate.recommenders.ironman.wizard.pages.editing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.MapItemElement;
import integrate.recommenders.ironman.definition.mapping.MapTargetElement;

public class EditingTargetWriteLangElements extends EditingSupport {
	
	private final MLMappingConfiguration mapping;
	
	public EditingTargetWriteLangElements(ColumnViewer viewer, MLMappingConfiguration mapping) {
		super(viewer);	
		this.mapping = mapping;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		final MapTargetElement targetElement = getKeyElement(element);
		final List<String> listOfCCElements = new ArrayList<String>();
		fillListOfEStructCC(targetElement.getTargetElement(),listOfCCElements);	
		return new ComboBoxCellEditor((Composite)getViewer().getControl(),listOfCCElements.toArray(new String[0]),
				SWT.BORDER 
				| SWT.READ_ONLY				
				);
	}

	@Override
	protected boolean canEdit(Object element) {
		final MapTargetElement targetElement = getKeyElement(element);
		if (targetElement.getTargetElement() == null)
			return false;
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof MapItemElement) {
			final MapTargetElement targetElement = getKeyElement(element);
			return targetElement
					.getTargetElement()
					.getEAllStructuralFeatures().indexOf(((MapItemElement)element).getWriteElement());
		}
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (value instanceof Integer && (Integer)value != -1 && element instanceof MapItemElement) {
			final MapTargetElement targetElement = getKeyElement(element);
			final EStructuralFeature strucFeat = targetElement.getTargetElement()
					.getEAllStructuralFeatures().get((Integer) value);
			((MapItemElement) element).setWriteElement(strucFeat);
			getViewer().update(element, null);
		}		
	}	
	
	private MapTargetElement getKeyElement(Object element) {
		return this.mapping.getSourceToTargetMap()
				.entrySet().stream()
				.filter(entry -> entry.getValue().contains(element))
				.findAny()
				.map(Map.Entry::getKey)														
				.orElseThrow(() -> new IllegalArgumentException("Unexpected element" + element.toString()));
	}
	
	private void fillListOfEStructCC(EClass eClass, List<String> listOfCCElements) {
		if (eClass != null) {
			eClass.getEAllStructuralFeatures().stream().map(eStruct -> eStruct.getName()).collect(Collectors.toCollection(() -> listOfCCElements));
		}
	}

}
