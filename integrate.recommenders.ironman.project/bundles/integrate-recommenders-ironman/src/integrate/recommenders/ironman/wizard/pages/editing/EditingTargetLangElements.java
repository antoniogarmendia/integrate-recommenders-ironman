package integrate.recommenders.ironman.wizard.pages.editing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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

public class EditingTargetLangElements extends EditingSupport {

	private final MLMappingConfiguration mapping;
	
	private EClass eClass;
	private EList<EClass> listOfEClasses;
	private EList<EStructuralFeature> listOfFeatures;
		
	public EditingTargetLangElements(ColumnViewer viewer, MLMappingConfiguration mapping) {
		super(viewer);		
		this.mapping = mapping;
		this.listOfEClasses = new BasicEList<EClass>();	
		this.listOfFeatures = new BasicEList<EStructuralFeature>();
		this.eClass = null;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		final EPackage ePackage = mapping.getEPackage();
		final List<String> listOfCCElements = new ArrayList<String>();
		if (element instanceof Entry) 
			fillListOfClassesCC(ePackage, listOfCCElements);
			else
				fillListOfEStructCC(listOfCCElements);
					
		return new ComboBoxCellEditor((Composite)getViewer().getControl(), listOfCCElements.toArray(new String[0]),
				SWT.BORDER 
				| SWT.READ_ONLY				
				);
	}

	private void fillListOfEStructCC(List<String> listOfCCElements) {
		if (eClass != null) {
			this.listOfFeatures = eClass.getEAllStructuralFeatures().stream().filter(eStruct -> 
									eStruct instanceof EAttribute 
									|| (eStruct instanceof EReference && ((EReference)eStruct).isContainment()))
									.collect(Collectors.toCollection(BasicEList::new));
			
			this.eClass.getEAllStructuralFeatures().stream().filter(eStruct -> 
						eStruct instanceof EAttribute 
						|| (eStruct instanceof EReference && ((EReference)eStruct).isContainment())
						)
						.map(eStruct -> eStruct.getName())
						.collect(Collectors.toCollection(() -> listOfCCElements));
		}
	}

	private void fillListOfClassesCC(final EPackage ePackage, final List<String> listOfClassNames) {
		if (ePackage != null) {
			this.listOfEClasses.clear();
			ePackage.getEClassifiers()
				.stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.collect(Collectors.toCollection(() -> listOfEClasses));
			
			ePackage.getEClassifiers()
					.stream()
					.map(eClass -> eClass.getName())
					.collect(Collectors.toCollection(() -> listOfClassNames));		
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			return listOfEClasses.indexOf(((MapTargetElement)entry.getKey()).getTargetElement());
		} else if (element instanceof MapItemElement){
			final MapItemElement mapItemElement = (MapItemElement) element;
			return this.listOfFeatures.indexOf(mapItemElement.getWriteElement());			
		}
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof Integer && ((Integer)element) != -1 ) {
			if (element instanceof Entry) {
				final Entry<?, ?> entry = (Entry<?, ?>) element;
				if ((Integer) value != -1) {
					this.eClass = this.listOfEClasses.get((Integer) value);
					((MapTargetElement)entry.getKey()).setTargetElement(eClass);
					getViewer().update(element, null);
				} 
			} else if (element instanceof MapItemElement) {
				final EStructuralFeature strucFeat = this.listOfFeatures.get((Integer) value);
				((MapItemElement) element).setWriteElement(strucFeat);
				getViewer().update(element, null);
			}
		}
	}	
	
}
