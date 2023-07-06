package integrate.recommenders.ironman.wizard.pages.editing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import integrate.recommenders.ironman.definition.mapping.AbstractItemElement;
import integrate.recommenders.ironman.definition.mapping.ActualFeature;
import integrate.recommenders.ironman.definition.mapping.MLMappingConfiguration;
import integrate.recommenders.ironman.definition.mapping.ReadFeature;
import integrate.recommenders.ironman.definition.mapping.TargetElement;
import integrate.recommenders.ironman.definition.mapping.TargetItemElement;
import integrate.recommenders.ironman.definition.mapping.WriteFeature;

public class EditingTargetLangElements extends EditingSupport {

	private final MLMappingConfiguration mapping;
	
	private EClass eClass;
	private EList<EClass> listOfEClasses;
	private EList<EStructuralFeature> listOfFeatures;
		
	public EditingTargetLangElements(ColumnViewer viewer, MLMappingConfiguration mapping) {
		super(viewer);		
		this.mapping = mapping;
		this.listOfEClasses = new BasicEList<>();	
		this.listOfFeatures = new BasicEList<>();
		this.eClass = null;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		final EPackage ePackage = mapping.getEPackage();
		final List<String> listOfCCElements = new ArrayList<String>();
		if (element instanceof Entry) 
			fillListOfClassesCC(ePackage, listOfCCElements);
			else if (element instanceof WriteFeature) 
				fillListOfEStructCC(listOfCCElements);
			else if (element instanceof ReadFeature)
				fillListOfEStruct(listOfCCElements);
			else if (element instanceof ActualFeature)
				fillListOfEAttributes(listOfCCElements, (ActualFeature) element);
		
		ComboBoxCellEditor comboEditor = new ComboBoxCellEditor((Composite)getViewer().getControl(), listOfCCElements.toArray(new String[0]),
				SWT.BORDER 
				| SWT.READ_ONLY				
				);	
		final Control control = comboEditor.getControl();
		if (control instanceof CCombo) {
			CCombo ccombo = (CCombo) control; 
			ccombo.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));
		}
		return comboEditor;
	}

	private void fillListOfEAttributes(List<String> listOfCCElements, ActualFeature actualFeature) {
		if (actualFeature.getParentTargetItemElement().getRead().getStructFeature() != null) {
			final EClassifier classifier  = actualFeature.getParentTargetItemElement().getRead().getStructFeature().getEType();
			Comparator<EAttribute> reverseOrderComparator =
					Comparator
		            .comparing(EAttribute::getName, Comparator.naturalOrder());
			if (classifier instanceof EClass) {
				final EClass eClass = (EClass) classifier;
				this.listOfFeatures = eClass.getEAllAttributes()
											.stream().map(EAttribute.class::cast)
											.sorted(reverseOrderComparator)
											.collect(Collectors.toCollection(BasicEList::new))
											;
				this.listOfFeatures.stream()
						.map(eStruct -> eStruct.getName())
						.collect(Collectors.toCollection(() -> listOfCCElements));
			}
		}		
	}

	private void fillListOfEStruct(List<String> listOfCCElements) {
		if (eClass != null) {			
			Comparator<EStructuralFeature> reverseOrderComparator =
					Comparator
		            .comparing(EStructuralFeature::getName, Comparator.naturalOrder());			
			this.listOfFeatures = eClass.getEAllStructuralFeatures().stream()
									.filter(struct -> struct instanceof EReference)
									.sorted(reverseOrderComparator)
									.collect(Collectors.toCollection(BasicEList::new));
			
			this.listOfFeatures.stream()
						.map(ENamedElement::getName)
						.collect(Collectors.toCollection(() -> listOfCCElements));
		}		
	}

	private void fillListOfEStructCC(List<String> listOfCCElements) {
		if (eClass != null) {
			Comparator<EStructuralFeature> reverseOrderComparator =
					Comparator
		            .comparing(EStructuralFeature::getName, Comparator.naturalOrder());
			
			this.listOfFeatures = eClass.getEAllStructuralFeatures().stream().filter(eStruct -> 
									eStruct instanceof EAttribute 
									|| (eStruct instanceof EReference && ((EReference)eStruct).isContainment()))
					 .sorted(reverseOrderComparator)
					 .collect(Collectors.toCollection(BasicEList::new))
					;
			
			this.listOfFeatures.stream()
						.map(eStruct -> eStruct.getName())
						.collect(Collectors.toCollection(() -> listOfCCElements));
		}
	}

	private void fillListOfClassesCC(final EPackage ePackage, final List<String> listOfClassNames) {
		if (ePackage != null) {
			this.listOfEClasses.clear();
			Comparator<EClass> reverseOrderComparator =
					Comparator
		            .comparing(EClass::getName, Comparator.naturalOrder());
			
			//Get all EClass. I did not use getEClassifiers because the use of subPackages
			//See IFML meta-model
			StreamSupport.stream(
			          Spliterators.spliteratorUnknownSize(ePackage.eAllContents(), Spliterator.ORDERED),
			          false)
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.sorted(reverseOrderComparator)
				.collect(Collectors.toCollection(() -> listOfEClasses));
			
			listOfEClasses
					.stream()
					.map(eClass -> eClass.getName())
					.collect(Collectors.toCollection(() -> listOfClassNames));		
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof  TargetItemElement)
			return false;
		return true;
	}

	@Override 
	protected Object getValue(Object element) {
		if (element instanceof Entry) {
			final Entry<?, ?> entry = (Entry<?, ?>) element;
			return listOfEClasses.indexOf(((TargetElement)entry.getKey()).getTargetElement());
		} 
		return 0;
	}

	@Override 
	protected void setValue(Object element, Object value) {
		if (value instanceof Integer && ((Integer) value) != -1) {
			if (element instanceof Entry) {
				final Entry<?, ?> entry = (Entry<?, ?>) element;
				if ((Integer) value != -1) {
					this.eClass = this.listOfEClasses.get((Integer) value);
					((TargetElement)entry.getKey()).setTargetElement(eClass);
					getViewer().update(element, null);
				} 
			}else if (element instanceof AbstractItemElement) {
				final EStructuralFeature strucFeat = this.listOfFeatures.get((Integer) value);
				((AbstractItemElement) element).setStructFeature(strucFeat);
				getViewer().update(element, null);
				getViewer().update(((AbstractItemElement) element).getParentTargetItemElement(), null);
			}
		}
	}	
	
}
