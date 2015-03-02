package com.sevenwatt.picos.templates.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.TemplateEngineHelper;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessHelper;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.cdt.core.templateengine.process.processes.Messages;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

/**
 * @author Doug Schaefer
 *
 * TODO - this is necessitated because the default macro format for
 * the template engine is $( and ) which is the same as make macros.
 * This replaces that with something more make friendly.
 * 
 * But at the end of they day, we need a real macro replacement engine
 * like JET, or something...
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class CopyMakeFile extends ProcessRunner {

	private static final String MAKEFILE = "Makefile"; //$NON-NLS-1$
	@Override
	public void process(TemplateCore template, ProcessArgument[] args,
			String processId, IProgressMonitor monitor)
			throws ProcessFailureException {
		String projectName = args[0].getSimpleValue();
		ProcessArgument file = args[1];
		ProcessArgument[] fileMembers = file.getComplexValue();
		String fileSourcePath = fileMembers[0].getSimpleValue();
		String fileTargetPath = fileMembers[1].getSimpleValue();
		boolean replaceable = fileMembers[2].getSimpleValue().equals("true"); //$NON-NLS-1$

		IProject projectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		URL path;
		try {
			path = TemplateEngineHelper.getTemplateResourceURLRelativeToTemplate(template, fileSourcePath);
			if (path == null) {
				throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFile.0") + fileSourcePath)); //$NON-NLS-1$
			}
		} catch (IOException e1) {
			throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFile.1") + MAKEFILE)); //$NON-NLS-1$
		}
		
		InputStream contents = null;
		if (replaceable) {
			String fileContents;
			try {
				fileContents = ProcessHelper.readFromFile(path);
			} catch (IOException e) {
				throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFile.2") + fileSourcePath)); //$NON-NLS-1$
			}
			Map<String, String> macros = new HashMap<String, String>(template.getValueStore());
			macros.put("exe", Platform.getOS().equals(Platform.OS_WIN32) ? ".exe" : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			fileContents = replaceMacros(fileContents, macros);
			contents = new ByteArrayInputStream(fileContents.getBytes());
		} else {
			try {
				contents = path.openStream();
			} catch (IOException e) {
				throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFile.3") + fileSourcePath)); //$NON-NLS-1$
			}
		}

		
		try {
			IFile iFile = projectHandle.getFile(fileTargetPath);
			if (!iFile.getParent().exists()) {
				ProcessHelper.mkdirs(projectHandle, projectHandle.getFolder(iFile.getParent().getProjectRelativePath()));
			}
			iFile.create(contents, true, null);
			iFile.refreshLocal(IResource.DEPTH_ONE, null);
			projectHandle.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFile.4") + e.getMessage()), e); //$NON-NLS-1$
		}
	}

	private static final String START = "{{"; //$NON-NLS-1$
	private static final String END = "}}"; //$NON-NLS-1$
	
	private String replaceMacros(String fileContents, Map<String, String> valueStore) {
		StringBuffer buffer = new StringBuffer(fileContents);
		for (String key : valueStore.keySet()) {
			String pattern = START + key +END;
			if (fileContents.indexOf(pattern)==-1)
				// Not used
				continue;
			
			// replace
			int len = pattern.length();
			int pos = 0;
			while ((pos = buffer.indexOf(pattern, pos)) >= 0) {
				buffer.replace(pos, pos + len, valueStore.get(key));
				pos += len;
			}
		}
		return buffer.toString();
	}
}