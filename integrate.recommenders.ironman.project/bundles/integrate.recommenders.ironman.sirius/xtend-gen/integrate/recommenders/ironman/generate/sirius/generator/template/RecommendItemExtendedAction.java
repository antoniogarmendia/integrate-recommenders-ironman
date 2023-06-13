package integrate.recommenders.ironman.generate.sirius.generator.template;

import integrate.recommenders.ironman.definition.services.Service;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.sirius.ExternalJavaActionTemplate;

@SuppressWarnings("all")
public class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
  private final Map<String, List<Service>> recommenderToServices;

  public RecommendItemExtendedAction(final String className, final String packageName, final Map<String, List<Service>> recommenderToServices) {
    super(className, packageName);
    this.recommenderToServices = recommenderToServices;
  }

  public CharSequence middleDefaultExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (!selectedElements.isEmpty()) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("final EObject selectedNode = selectedElements.stream().findFirst().get();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (selectedNode instanceof DNodeList) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("var nodeList = (DNodeList) selectedNode;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("final EObject targetEObject = nodeList.getTarget();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//TODO generate target EClass or Class");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (targetEObject instanceof EClass) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//TODO EClass name is not a reflective...");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final EClass eClass = ((EClass) targetEObject);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final String name = eClass.getName();");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//TODO Call all the recommenders");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final RecommenderCase recommenderCase = getRecommenderCase(name);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final Map<String, List<ItemRecommender>> recServerToItemRecommenders = getAllRecommendations(recommenderCase);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//Merge ");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final Map<String, Double> dataFusion = EvaluateMetaSearchContributionHandler.");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("executeMetaSearchAlgorithByName(\"OutRankingApproach\", recServerToItemRecommenders);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final Map<String, Double> normalizeDataFusion = normalization(dataFusion);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("//Graphical Interface");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final RecommenderDialog recDialog = new RecommenderDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append(", recServerToItemRecommenders, normalizeDataFusion, \"EClass\");");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (recDialog.open() == Window.OK) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("//Add selected elements");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("addSelectRecommendation(recDialog.getSelectedRecommendations(),eClass);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
}
