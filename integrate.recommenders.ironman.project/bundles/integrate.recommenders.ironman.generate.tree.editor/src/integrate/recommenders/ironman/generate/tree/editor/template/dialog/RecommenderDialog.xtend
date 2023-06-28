package integrate.recommenders.ironman.generate.tree.editor.template.dialog

import project.generator.api.template.IGeneration

class RecommenderDialog implements IGeneration {
	
	val String packageName;
	val String packageNameUtils;
	
	new(String packageName, String packageNameUtils) {
		this.packageName = packageName;
		this.packageNameUtils = packageNameUtils;
	}
	
	override doGenerate() {
		'''
			package «packageName»;
			
			import java.util.Arrays;
			import java.util.List;
			import java.util.Map;
			import java.util.stream.Collectors;
			
			import org.eclipse.jface.dialogs.Dialog;
			import org.eclipse.jface.layout.GridDataFactory;
			import org.eclipse.jface.viewers.ArrayContentProvider;
			import org.eclipse.jface.viewers.CheckboxTableViewer;
			import org.eclipse.jface.viewers.TableViewerColumn;
			import org.eclipse.jface.widgets.LabelFactory;
			import org.eclipse.swt.SWT;
			import org.eclipse.swt.widgets.Composite;
			import org.eclipse.swt.widgets.Control;
			import org.eclipse.swt.widgets.Shell;
			
			import static «this.packageNameUtils».RecommenderUtils.*;
			import integrate.recommenders.ironman.definition.recommenders.ItemRecommender;
			
			public class RecommenderDialog extends Dialog {
				
				private final String target;
				private final String property;
				private CheckboxTableViewer tableOfRecommendations;
				private final List<RecommenderData> recommendations;
				private List<RecommenderData> selectedRecommendations;
				
				public RecommenderDialog(Shell parentShell, Map<String, List<ItemRecommender>> recServerToItemRecommenders, 
						Map<String, Double> normalizeDataFusion, String target, String property) {
					super(parentShell);		
					this.target = target;
					this.property = property;
					this.recommendations = convertToOrderedListOfRecommendations(recServerToItemRecommenders, normalizeDataFusion);		
				}
				«createDialogArea»
				«configureShell»
				«selectedRecommendations»		
				«createResizable»	
			}
		'''
	}
	
	def createResizable() {
		'''
			
			@Override
			protected boolean isResizable() {
			  return true;
			}
		'''
	}
	
	def createDialogArea() {
		'''
			
			@Override
			protected Control createDialogArea(Composite parent) {
				final Composite container = (Composite) super.createDialogArea(parent);
				LabelFactory.newLabel(SWT.NONE).text("Select Recommendations").create(container);
				//CheckTableViewer
				tableOfRecommendations = CheckboxTableViewer.newCheckList(container, SWT.NONE);
				tableOfRecommendations.getTable().setLinesVisible(true);
				tableOfRecommendations.getTable().setHeaderVisible(true);
				tableOfRecommendations.setUseHashlookup(true);
				
				tableOfRecommendations.getTable().setLayoutData(GridDataFactory.fillDefaults().create());
				
				//Name Column
				final TableViewerColumn nameColumn = new TableViewerColumn(tableOfRecommendations, SWT.CENTER);
				nameColumn.getColumn().setWidth(180);
				nameColumn.getColumn().setText("Name");
				nameColumn.setLabelProvider(new NameColumLabelProvider());
				
				//Recommender Column
				final TableViewerColumn recColumn = new TableViewerColumn(tableOfRecommendations, SWT.CENTER);
				recColumn.getColumn().setWidth(320);
				recColumn.getColumn().setText("Recommenders");
				recColumn.setLabelProvider(new RecColumLabelProvider());
					
				//Recommender Column
				final TableViewerColumn ratingColumn = new TableViewerColumn(tableOfRecommendations, SWT.CENTER);
				ratingColumn.getColumn().setWidth(180);
				ratingColumn.getColumn().setText("Rating");
				ratingColumn.setLabelProvider(new RatingColumLabelProvider());
				
				
				tableOfRecommendations.setContentProvider(ArrayContentProvider.getInstance());	
				tableOfRecommendations.setInput(this.recommendations);
						
				return super.createDialogArea(parent);
			}
		'''
	}
	
	def getSelectedRecommendations() {
		'''
			public List<RecommenderData> getSelectedRecommendations() {		
					return selectedRecommendations;		
			}
				
			@Override
			protected void okPressed() {		
				selectedRecommendations = Arrays.stream(tableOfRecommendations.getCheckedElements())
												.map(RecommenderData.class::cast)
												.collect(Collectors.toList());
				super.okPressed();
			}
		'''
	}
	
	def String configureShell(){
		'''
			
			// Set the title of the dialog
			@Override
			protected void configureShell(Shell newShell) {
				super.configureShell(newShell);
				newShell.setText("Recommend " + this.property + " for " + this.target);
			}
		'''
	}
	
}
