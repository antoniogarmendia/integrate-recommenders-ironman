package integrate.recommenders.ironman.generate.sirius.generator.template;

import integrate.recommenders.ironman.definition.services.Recommender;
import integrate.recommenders.ironman.definition.services.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import project.generator.api.template.IGeneration;

@SuppressWarnings("all")
public class RecommenderUtils implements IGeneration {
  private final String packageName;

  private final Map<String, List<Service>> recommenderToServices;

  public RecommenderUtils(final String packageName, final Map<String, List<Service>> recommenderToServices) {
    this.packageName = packageName;
    this.recommenderToServices = recommenderToServices;
  }

  public String doGenerate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(this.packageName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public final class RecommenderUtils {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private RecommenderUtils() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("throw new AssertionError(\"This class does not allow inheritance\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//URLs of the Recommenders");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _recommenderURLs = this.recommenderURLs();
    _builder.append(_recommenderURLs, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}\t\t\t");
    _builder.newLine();
    return _builder.toString();
  }

  public CharSequence recommenderURLs() {
    StringConcatenation _builder = new StringConcatenation();
    int i = 1;
    _builder.newLineIfNotEmpty();
    {
      Set<Map.Entry<String, List<Service>>> _entrySet = this.recommenderToServices.entrySet();
      for(final Map.Entry<String, List<Service>> entryRecommend : _entrySet) {
        _builder.append("public static final String URL_RECOMMENDER_");
        int _plusPlus = i++;
        _builder.append(_plusPlus);
        _builder.append(" = \"");
        String _key = entryRecommend.getKey();
        _builder.append(_key);
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        {
          List<Service> _value = entryRecommend.getValue();
          for(final Service services : _value) {
            {
              List<Recommender> _services = services.getServices();
              for(final Recommender service : _services) {
                _builder.append("public static final String RECOMMENDER_");
                _builder.append(i);
                _builder.append("_");
                String _name = service.getName();
                _builder.append(_name);
                _builder.append(" = \"");
                String _name_1 = service.getName();
                _builder.append(_name_1);
                _builder.append("\";");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
}
