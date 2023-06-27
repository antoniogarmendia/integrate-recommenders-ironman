package project.generator.api.utils;

public class BuilderUtils {
	
	// Suppress default constructor for noninstantiability
	private BuilderUtils() {
		throw new AssertionError();
	}
	
	//Schema
	public static final String MANIFEST_BUILDER = "org.eclipse.pde.ManifestBuilder";
	public static final String SCHEMA_BUILDER = "org.eclipse.pde.SchemaBuilder";
	
	public static final String REQUIRED_PLUGIN = "org.eclipse.pde.core.requiredPlugins";
	// Folders
	public static final String SOURCE_FOLDER = "src";
	public static final String MAIN_FOLDER = "main";
	public static final String TARGET_FOLDER = "target";
	public static final String CLASSES_FOLDER = "classes";
	public static final String JAVA_FOLDER = "java";
}
