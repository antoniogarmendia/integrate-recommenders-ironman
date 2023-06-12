package project.generator.api.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

public class WriteUtils {
	
	// Suppress default constructor for noninstantiability
	private WriteUtils() {
		throw new AssertionError();
	}	
	
	public static void write(IContainer container, String fileName, String content) {
		write(container, fileName, content, true);
	}
	
	public static void write(IContainer container, String fileName, String content, boolean replace) {
		final var file = container.getFile(new Path(fileName));
		if (file.exists() && replace) {
			try {
				file.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		if (!file.exists()) {
			final var bytes = content.getBytes();
			InputStream source = new ByteArrayInputStream(bytes);
			try {
				if (!file.getParent().exists()) {
					createFolder((IFolder) file.getParent(), true, true, null);
				}
				file.create(source, IResource.NONE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public static IFolder createFolder(IFolder folder, boolean force, boolean local, IProgressMonitor monitor)
			throws CoreException {
		if (!folder.exists()) {
			IContainer parent = folder.getParent();
			if (parent.exists() == false) {
				createFolder((IFolder) parent, force, local, null);
			}
			folder.create(force, local, monitor);
		}
		return folder;
	}
	
}
