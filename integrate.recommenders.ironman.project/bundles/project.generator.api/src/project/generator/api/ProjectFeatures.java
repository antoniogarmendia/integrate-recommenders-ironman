package project.generator.api;

public class ProjectFeatures {
	
	//Required parameters
	private final String projectName;
			
	//Optional parameters
	private final boolean isJavaProject;
	private final boolean isPlugin;
	private final String javaSE;
	
	public static class Builder {
		
	//Required parameters
	private final String projectName;
				
	//Optional parameters
	private boolean isJavaProject;
	private boolean isPlugin;
	private String javaSE;
	
	public Builder(String projectName) {
		this.projectName = projectName;
	}
	
	public Builder isJavaProject(boolean isJavaProject) {
		this.isJavaProject = isJavaProject;
		return this;
	}
		
	public Builder isPlugin(boolean isPlugin) {
		this.isPlugin = isPlugin;
		return this;
	}
		
	public Builder javaSE(String javaSE) {
		this.javaSE = javaSE;
		return this;
	}
		
	public ProjectFeatures build() {
		return new ProjectFeatures(this);
	}
}
	
	private ProjectFeatures(Builder builder) {
		projectName = builder.projectName;
		isJavaProject = builder.isJavaProject;
		isPlugin = builder.isPlugin;
		javaSE = builder.javaSE;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public boolean isJavaProject() {
		return isJavaProject;
	}

	public boolean isPlugin() {
		return isPlugin;
	}

	public String getJavaSE() {
		return javaSE;
	}	
}
