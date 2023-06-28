package project.generator.api.template;

import org.eclipse.xtend2.lib.StringConcatenation;

public abstract class AbstractPluginXMLTemplate implements IGeneration {
  public java.lang.CharSequence begin() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<?eclipse version=\"3.4\"?>");
    _builder.newLine();
    _builder.append("<plugin>");
    _builder.newLine();
    return _builder;
  }

  public abstract /* String */Object middle();

  public String doGenerate() {
    throw new Error("Unresolved compilation problems:"
      + "\nStringBuilder cannot be resolved to a type."
      + "\nStringBuilder cannot be resolved."
      + "\nThe method begin() from the type AbstractPluginXMLTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method middle() from the type AbstractPluginXMLTemplate refers to the missing type String"
      + "\nThe method end() from the type AbstractPluginXMLTemplate refers to the missing type java.lang.CharSequence"
      + "\nappend cannot be resolved"
      + "\nappend cannot be resolved"
      + "\nappend cannot be resolved"
      + "\ntoString cannot be resolved");
  }

  public java.lang.CharSequence end() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("</plugin>");
    _builder.newLine();
    return _builder;
  }
}
