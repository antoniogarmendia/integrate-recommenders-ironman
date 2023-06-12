package project.generator.api.template.sirius;

import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.AbstractPluginXMLTemplate;

@SuppressWarnings("all")
public class ViewpointEditorPluginXML extends AbstractPluginXMLTemplate {
  private final IProject project;

  private final Set<String> items;

  private final String packageName;

  public ViewpointEditorPluginXML(final IProject project, final String packageName, final Set<String> items) {
    this.project = project;
    this.items = items;
    this.packageName = packageName;
  }

  public String middle() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<extension point=\"org.eclipse.sirius.componentization\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<component class=\"");
    String _name = this.project.getName();
    _builder.append(_name, "    ");
    _builder.append(".Activator\"");
    _builder.newLineIfNotEmpty();
    _builder.append("               ");
    _builder.append("id=\"");
    String _name_1 = this.project.getName();
    _builder.append(_name_1, "               ");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("name=\"");
    String _name_2 = this.project.getName();
    _builder.append(_name_2, "        ");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("</component>");
    _builder.newLine();
    _builder.append("</extension>");
    _builder.newLine();
    _builder.append("<extension");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("point=\"org.eclipse.sirius.externalJavaAction\">");
    _builder.newLine();
    {
      for(final String item : this.items) {
        _builder.append("        ");
        _builder.append("<javaActions");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("\t ");
        _builder.append("actionClass=\"");
        _builder.append(this.packageName, "        \t ");
        _builder.append(".Recommend");
        _builder.append(item, "        \t ");
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("\t ");
        _builder.append("id=\"");
        _builder.append(this.packageName, "        \t ");
        _builder.append(".");
        _builder.append(item, "        \t ");
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("</javaActions>");
        _builder.newLine();
      }
    }
    _builder.append("  ");
    _builder.append("</extension>");
    _builder.newLine();
    return _builder.toString();
  }
}
