package project.generator.api.template;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public abstract class AbstractPluginXMLTemplate implements IGeneration {
  public CharSequence begin() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<?eclipse version=\"3.4\"?>");
    _builder.newLine();
    _builder.append("<plugin>");
    _builder.newLine();
    return _builder;
  }

  public abstract String middle();

  public String doGenerate() {
    final StringBuilder content = new StringBuilder();
    content.append(this.begin());
    content.append(this.middle());
    content.append(this.end());
    return content.toString();
  }

  public CharSequence end() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("</plugin>");
    _builder.newLine();
    return _builder;
  }
}
