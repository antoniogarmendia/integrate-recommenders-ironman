package integrate.recommenders.ironman.generate.sirius.generator.template;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.MetaInfTemplate;
import project.generator.api.utils.GenModelUtils;

@SuppressWarnings("all")
public class MetaInfRecommender extends MetaInfTemplate {
  private final IProject project;

  private final Map<String, List<Service>> recommenderToServices;

  public MetaInfRecommender(final IProject project, final Map<String, List<Service>> recommenderToServices) {
    this.project = project;
    this.recommenderToServices = recommenderToServices;
  }

  public String bundleNameSymbolic() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-Name: ProjectTreeIntegration");
    _builder.newLine();
    _builder.append("Bundle-SymbolicName: ");
    String _name = this.project.getName();
    _builder.append(_name);
    _builder.append(";singleton:=true");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String exportPackage() {
    return "";
  }

  public String requireBundle() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Require-Bundle: org.eclipse.ui,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.core.runtime,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.core.resources,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.sirius,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.sirius.common.acceleo.aql,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.sirius.diagram,");
    _builder.newLine();
    {
      Set<Map.Entry<String, List<Service>>> _entrySet = this.recommenderToServices.entrySet();
      for(final Map.Entry<String, List<Service>> service : _entrySet) {
        {
          List<Service> _value = service.getValue();
          for(final Service serv : _value) {
            {
              List<Recommender> _services = serv.getServices();
              for(final Recommender rec : _services) {
                {
                  EClass _targetEClass = rec.getDetails().getTargetEClass();
                  boolean _not = (!(_targetEClass instanceof EClass));
                  if (_not) {
                    String _projectFromEClass = GenModelUtils.getProjectFromEClass(rec.getDetails().getTargetEClass());
                    _builder.append(_projectFromEClass);
                    _builder.append("\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append(" ");
    _builder.append("org.eclipse.emf");
    _builder.newLine();
    return _builder.toString();
  }

  public String automaticModuleName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Automatic-Module-Name: ");
    String _name = this.project.getName();
    _builder.append(_name);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String requiredExecutionEnvironment() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-RequiredExecutionEnvironment: JavaSE-17");
    _builder.newLine();
    return _builder.toString();
  }
}
