package integrate.recommenders.ironman.generate.sirius.generator.template;

import integrate.recommenders.ironman.definition.services.Service;
import java.util.List;
import java.util.Map;
import project.generator.api.template.sirius.ExternalJavaActionTemplate;

@SuppressWarnings("all")
public class RecommendItemExtendedAction extends ExternalJavaActionTemplate {
  private final Map<String, List<Service>> recommenderToServices;

  public RecommendItemExtendedAction(final String className, final String packageName, final Map<String, List<Service>> recommenderToServices) {
    super(className, packageName);
    this.recommenderToServices = recommenderToServices;
  }
}
