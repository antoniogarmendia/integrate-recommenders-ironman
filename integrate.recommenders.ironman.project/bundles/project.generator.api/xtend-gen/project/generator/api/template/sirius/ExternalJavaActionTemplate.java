package project.generator.api.template.sirius;

import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.AbstractJavaClass;
import project.generator.api.template.IGeneration;

public abstract class ExternalJavaActionTemplate extends AbstractJavaClass implements IGeneration {
  public ExternalJavaActionTemplate(final /* String */Object className, final /* String */Object packageName) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe constructor AbstractJavaClass(String, String) refers to the missing type String");
  }

  public String doGenerate() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method importDependencies() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method start() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method startConstructor() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method middleConstructor() from the type ExternalJavaActionTemplate refers to the missing type java.lang.Object"
      + "\nThe method endConstructor() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method startCanExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method middleDefaultCanExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method endCanExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method startExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method middleDefaultExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method endExecute() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence"
      + "\nThe method restOfMethods() from the type ExternalJavaActionTemplate refers to the missing type String"
      + "\nThe method end() from the type ExternalJavaActionTemplate refers to the missing type java.lang.CharSequence");
  }

  public abstract /* String */Object restOfMethods();

  public java.lang.CharSequence importDependencies() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getPackageName() from the type AbstractJavaClass refers to the missing type String");
  }

  public java.lang.CharSequence start() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getClassName() from the type AbstractJavaClass refers to the missing type String");
  }

  public java.lang.CharSequence end() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public java.lang.CharSequence startConstructor() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getClassName() from the type AbstractJavaClass refers to the missing type String");
  }

  public java.lang.Object middleConstructor() {
    return null;
  }

  public java.lang.CharSequence endConstructor() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public java.lang.CharSequence startCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public boolean canExecute(Collection<? extends EObject> arg0) {");
    _builder.newLine();
    return _builder;
  }

  public java.lang.CharSequence middleDefaultCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return true;");
    _builder.newLine();
    return _builder;
  }

  public java.lang.CharSequence endCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public java.lang.CharSequence startExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void execute(Collection<? extends EObject> selectedElements, Map<String, Object> parameters) {");
    _builder.newLine();
    return _builder;
  }

  public java.lang.CharSequence middleDefaultExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// TODO Auto-generated method stub");
    _builder.newLine();
    return _builder;
  }

  public java.lang.CharSequence endExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
