package integrate.recommenders.ironman.generate.sirius.generator.template;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.MetaInfTemplate;

@SuppressWarnings("all")
public class MetaInfRecommender extends MetaInfTemplate {
  private final IProject project;

  public MetaInfRecommender(final IProject project) {
    this.project = project;
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
