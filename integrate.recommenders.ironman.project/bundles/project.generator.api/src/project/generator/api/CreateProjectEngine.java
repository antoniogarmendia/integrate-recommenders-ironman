package project.generator.api;

import java.util.ArrayList;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;

import project.generator.api.utils.BuilderUtils;
import project.generator.api.utils.NatureUtils;

public class CreateProjectEngine {
	
	private final ProjectFeatures projectFeat;
	private IProject project;
	
	public CreateProjectEngine(ProjectFeatures projectFeat) {
		this.projectFeat = projectFeat;
		this.project = null;
	}
	
	public IProject doGenerateProject(IProgressMonitor monitor, boolean delete) {
		final IWorkspaceRunnable createProject = new IWorkspaceRunnable() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				if( delete && exists()) {
					deleteProject(monitor);
				}
				createProject(monitor);				
			}			
		};
		try {
			ResourcesPlugin.getWorkspace().run(createProject, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return project;
	}
	
	private void createProject(IProgressMonitor monitor) {
		this.project = ResourcesPlugin.getWorkspace().getRoot().getProject(this.projectFeat.getProjectName());
		if (!this.project.exists()) {		
	    	try {
	    		this.project.create(monitor);
	    		this.project.open(monitor);
				addNatures(monitor);
				addSchemaBuilders(monitor);
				createFolders(monitor);
				configureClassPath(monitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}	
		
	}
	
	private void configureClassPath(IProgressMonitor monitor) {
		var javaPro = JavaCore.create(this.project);
		final var entries = new ArrayList<IClasspathEntry>();
		final var executionEnvironmentsManager = JavaRuntime.getExecutionEnvironmentsManager();
		final var executionEnvironments = executionEnvironmentsManager.getExecutionEnvironments();	
		//If the JAVASE is null then just add the default JRE
		if (this.projectFeat.getJavaSE() != null) {
			for (IExecutionEnvironment iExecutionEnvironment : executionEnvironments) {
			    // We will look for JavaSE-11 as the JRE container to add to our classpath
				 if (this.projectFeat.getJavaSE().equals(iExecutionEnvironment.getId())) {
				       entries.add(JavaCore.newContainerEntry(JavaRuntime.newJREContainerPath(iExecutionEnvironment)));
			        break;
			    }
			}
		} else
			entries.add(JavaRuntime.getDefaultJREContainerEntry());			
			
		configureClassPathPlugin(javaPro, entries, monitor);
	}

	private void configureClassPathPlugin(IJavaProject javaPro, ArrayList<IClasspathEntry> entries, IProgressMonitor monitor) {
		try {
			javaPro.setOutputLocation(new Path("/" + this.projectFeat.getProjectName() + "/bin"), monitor);
			final IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(new Path("/" + this.projectFeat.getProjectName() + "/" + BuilderUtils.SOURCE_FOLDER));
			entries.add(0, srcClasspathEntry);		    
			entries.add(JavaCore.newContainerEntry(new Path(BuilderUtils.REQUIRED_PLUGIN)));		    
	   
			javaPro.setRawClasspath(entries.toArray(new IClasspathEntry[0]),
			      monitor);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}		
	}

	private void createFolders(IProgressMonitor monitor) {
		if (this.projectFeat.isPlugin() || this.projectFeat.isJavaProject()) {
			createSrcFolder(monitor);
		}		
	}

	private void createSrcFolder(IProgressMonitor monitor) {
		createFolder(this.project, "src", monitor);		
	}
	
	private void addSchemaBuilders(IProgressMonitor monitor) {
		if (this.projectFeat.isJavaProject() || this.projectFeat.isPlugin()) {
			final var builders = new ArrayList<ICommand>();
			ICommand java;
			try {
				java = this.project.getDescription().newCommand();
				java.setBuilderName(JavaCore.BUILDER_ID);
		        builders.add(java);
		        
		        if (this.projectFeat.isPlugin()) {
		        	final ICommand manifest = this.project.getDescription().newCommand();
		            manifest.setBuilderName(BuilderUtils.MANIFEST_BUILDER);
		            builders.add(manifest);

		            final ICommand schema = this.project.getDescription().newCommand();
		            schema.setBuilderName(BuilderUtils.SCHEMA_BUILDER);
		            builders.add(schema);
		        }
		        ICommand[] buildersSpecs = builders.toArray(new ICommand[builders.size()]);
		        this.project.getDescription().setBuildSpec(buildersSpecs);
			} catch (CoreException e) {
				e.printStackTrace();
			}			
		}	
		
	}

	private void addNatures(IProgressMonitor monitor) {
		final IProjectDescription description;
		try {
			description = this.project.getDescription();
			description.setNatureIds(getNatures());
			this.project.setDescription(description, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}

	private void deleteProject(IProgressMonitor monitor) {
		IProject pro = ResourcesPlugin.getWorkspace().getRoot().getProject(this.projectFeat.getProjectName());
		try {
			pro.delete(true, new NullProgressMonitor());
			pro.getParent().refreshLocal(IProject.DEPTH_INFINITE,
					new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}

	private boolean exists() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(this.projectFeat.getProjectName()).exists();
	}
	
	private String[] getNatures() {
		final var listOfNatures = new ArrayList<String>();
		if (this.projectFeat.isPlugin()) {
			listOfNatures.add(JavaCore.NATURE_ID);
			listOfNatures.add(NatureUtils.PLUGIN_NATURE);
		}
		
		return listOfNatures.toArray(new String[0]);
	}
	
	private IFolder createFolder(final IContainer container, final String folderName, final IProgressMonitor monitor) {		
		IFolder newFolder = container.getFolder(new Path(folderName));
		try {
			if (newFolder.exists())
				return newFolder;
			newFolder.create(true, true, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return newFolder;
	}
}
