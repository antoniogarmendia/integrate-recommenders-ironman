package project.generator.api.template;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public abstract class MetaInfTemplate implements IGeneration {
  public CharSequence begin() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Manifest-Version: 1.0");
    _builder.newLine();
    _builder.append("Bundle-ManifestVersion: 2");
    _builder.newLine();
    String _bundleNameSymbolic = this.bundleNameSymbolic();
    _builder.append(_bundleNameSymbolic);
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-Version: 1.0.0.qualifier");
    _builder.newLine();
    return _builder;
  }

  public abstract String bundleNameSymbolic();

  public abstract String exportPackage();

  public abstract String requireBundle();

  public abstract String automaticModuleName();

  public abstract String requiredExecutionEnvironment();

  public String doGenerate() {
    final StringBuilder content = new StringBuilder();
    content.append(this.begin());
    content.append(this.exportPackage());
    content.append(this.requireBundle());
    content.append(this.automaticModuleName());
    content.append(this.requiredExecutionEnvironment());
    return content.toString();
  }
}
