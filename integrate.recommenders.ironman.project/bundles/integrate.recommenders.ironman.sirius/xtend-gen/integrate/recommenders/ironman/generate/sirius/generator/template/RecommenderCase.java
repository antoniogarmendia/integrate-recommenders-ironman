package integrate.recommenders.ironman.generate.sirius.generator.template;

import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.IGeneration;

@SuppressWarnings("all")
public class RecommenderCase implements IGeneration {
  private final String packageName;

  public RecommenderCase(final String packageName) {
    this.packageName = packageName;
  }

  public String doGenerate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(this.packageName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import java.util.Collection;");
    _builder.newLine();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class RecommenderCase {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Map<String, Collection<String>> urlToRecommenders;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private String type;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private String targetName;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public RecommenderCase(Map<String, Collection<String>> urlToRecommenders, String type, String targetName) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.urlToRecommenders = urlToRecommenders;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.type = type;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.targetName = targetName;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getType() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return type;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public Map<String, Collection<String>> getUrlToRecommenders() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return urlToRecommenders;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getTargetName() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return targetName;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
