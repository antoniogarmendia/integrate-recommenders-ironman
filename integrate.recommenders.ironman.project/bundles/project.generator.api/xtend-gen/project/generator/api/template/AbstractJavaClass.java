package project.generator.api.template;

@SuppressWarnings("all")
public abstract class AbstractJavaClass {
  private final String className;

  private final String packageName;

  public AbstractJavaClass(final String className, final String packageName) {
    this.className = className;
    this.packageName = packageName;
  }

  public String getClassName() {
    return this.className;
  }

  public String getPackageName() {
    return this.packageName;
  }
}
