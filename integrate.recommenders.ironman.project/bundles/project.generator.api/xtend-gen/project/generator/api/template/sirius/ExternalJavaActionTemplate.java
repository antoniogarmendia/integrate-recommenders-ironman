package project.generator.api.template.sirius;

import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.AbstractJavaClass;
import project.generator.api.template.IGeneration;

@SuppressWarnings("all")
public abstract class ExternalJavaActionTemplate extends AbstractJavaClass implements IGeneration {
  public ExternalJavaActionTemplate(final String className, final String packageName) {
    super(className, packageName);
  }

  public String doGenerate() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _importDependencies = this.importDependencies();
    _builder.append(_importDependencies);
    _builder.newLineIfNotEmpty();
    CharSequence _start = this.start();
    _builder.append(_start);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _startConstructor = this.startConstructor();
    _builder.append(_startConstructor, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    Object _middleConstructor = this.middleConstructor();
    _builder.append(_middleConstructor, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _endConstructor = this.endConstructor();
    _builder.append(_endConstructor, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _startCanExecute = this.startCanExecute();
    _builder.append(_startCanExecute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _middleDefaultCanExecute = this.middleDefaultCanExecute();
    _builder.append(_middleDefaultCanExecute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _endCanExecute = this.endCanExecute();
    _builder.append(_endCanExecute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _startExecute = this.startExecute();
    _builder.append(_startExecute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _middleDefaultExecute = this.middleDefaultExecute();
    _builder.append(_middleDefaultExecute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _endExecute = this.endExecute();
    _builder.append(_endExecute, "\t");
    _builder.newLineIfNotEmpty();
    CharSequence _end = this.end();
    _builder.append(_end);
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public CharSequence importDependencies() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _packageName = this.getPackageName();
    _builder.append(_packageName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import java.util.Collection;");
    _builder.newLine();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import org.eclipse.emf.ecore.EObject;");
    _builder.newLine();
    _builder.append("import org.eclipse.sirius.diagram.DNodeList;");
    _builder.newLine();
    _builder.append("import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;\t\t");
    _builder.newLine();
    return _builder;
  }

  public CharSequence start() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("public class ");
    String _className = this.getClassName();
    _builder.append(_className);
    _builder.append(" implements IExternalJavaAction {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public CharSequence end() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public CharSequence startConstructor() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _className = this.getClassName();
    _builder.append(_className);
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public Object middleConstructor() {
    return null;
  }

  public CharSequence endConstructor() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public CharSequence startCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public boolean canExecute(Collection<? extends EObject> arg0) {");
    _builder.newLine();
    return _builder;
  }

  public CharSequence middleDefaultCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return true;");
    _builder.newLine();
    return _builder;
  }

  public CharSequence endCanExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    return _builder;
  }

  public CharSequence startExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void execute(Collection<? extends EObject> selectedElements, Map<String, Object> parameters) {");
    _builder.newLine();
    return _builder;
  }

  public CharSequence middleDefaultExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// TODO Auto-generated method stub");
    _builder.newLine();
    return _builder;
  }

  public CharSequence endExecute() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
