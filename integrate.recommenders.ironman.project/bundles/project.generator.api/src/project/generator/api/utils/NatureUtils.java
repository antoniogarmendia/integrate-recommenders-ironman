package project.generator.api.utils;

public class NatureUtils {

	// Suppress default constructor for noninstantiability
	private NatureUtils() {
		throw new AssertionError();
	}
	
	public static final String MAVEN_NATURE = "org.eclipse.m2e.core.maven2Nature";
	public static final String PLUGIN_NATURE = "org.eclipse.pde.PluginNature";
}
