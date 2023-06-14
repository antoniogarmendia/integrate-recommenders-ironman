package integrate.recommenders.ironman.generate.sirius.generator.template;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.sirius.ExternalJavaActionTemplate;
import project.generator.api.utils.GenModelUtils;

@SuppressWarnings("all")
public class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
  private final Map<String, List<Service>> recommenderToServices;

  private final String item;

  private final Map.Entry<String, List<Service>> service;

  private Recommender recommender;

  public RecommendItemExtendedAction(final String className, final String packageName, final String item, final Map<String, List<Service>> recommenderToServices) {
    super(className, packageName);
    this.recommenderToServices = recommenderToServices;
    this.item = item;
    this.service = this.getService();
  }

  public Map.Entry<String, List<Service>> getService() {
    final Predicate<Map.Entry<String, List<Service>>> _function = new Predicate<Map.Entry<String, List<Service>>>() {
      public boolean test(final Map.Entry<String, List<Service>> listOfServ) {
        final Predicate<Service> _function = new Predicate<Service>() {
          public boolean test(final Service serv) {
            return RecommendItemExtendedAction.this.existRecommender(serv);
          }
        };
        long _count = listOfServ.getValue().stream().filter(_function).count();
        return (_count > 0);
      }
    };
    return this.recommenderToServices.entrySet().stream().filter(_function).collect(Collectors.<Map.Entry<String, List<Service>>>toList()).get(0);
  }

  public boolean existRecommender(final Service service) {
    final Predicate<Recommender> _function = new Predicate<Recommender>() {
      public boolean test(final Recommender r) {
        return r.getDetails().getItems().contains(RecommendItemExtendedAction.this.item);
      }
    };
    final Recommender rec = service.getServices().stream().filter(_function).findAny().orElse(null);
    if ((rec != null)) {
      this.recommender = rec;
      return true;
    }
    return false;
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
    _builder.append("if (targetEObject instanceof ");
    String _name = this.recommender.getDetails().getTargetEClass().getName();
    _builder.append(_name, "\t\t");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("final ");
    String _name_1 = this.recommender.getDetails().getTargetEClass().getName();
    _builder.append(_name_1, "\t\t\t");
    _builder.append(" target = ((EClass) targetEObject);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("//final String target = eClass.getName();");
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

  public CharSequence importDependencies() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _importDependencies = super.importDependencies();
    _builder.append(_importDependencies);
    _builder.newLineIfNotEmpty();
    _builder.append("import ");
    String _packageClassFromEClass = GenModelUtils.getPackageClassFromEClass(this.recommender.getDetails().getTargetEClass());
    _builder.append(_packageClassFromEClass);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
