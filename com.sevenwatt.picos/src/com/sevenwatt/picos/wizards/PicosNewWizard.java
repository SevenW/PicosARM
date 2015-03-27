package com.sevenwatt.picos.wizards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.gdbjtag.core.IGDBJtagConstants;
import org.eclipse.cdt.debug.mi.core.IMILaunchConfigurationConstants;
import org.eclipse.cdt.debug.mi.core.MIPlugin;
import org.eclipse.cdt.debug.mi.core.command.factories.CommandFactoryDescriptor;
import org.eclipse.cdt.debug.mi.core.command.factories.CommandFactoryManager;
import org.eclipse.cdt.dsf.gdb.IGDBLaunchConfigurationConstants;
//import org.eclipse.cdt.internal.ui.newui.Messages;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.wizards.CDTCommonProjectWizard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.graphics.Image;

/**
 * The wizard to create new MBS C++ Project.
 */
public class PicosNewWizard extends CDTCommonProjectWizard {
	private static final String PREFIX= "CProjectWizard"; //$NON-NLS-1$
	private static final String wz_title = org.eclipse.cdt.internal.ui.newui.Messages.NewModelProjectWizard_2;
	private static final String wz_desc = org.eclipse.cdt.internal.ui.newui.Messages.NewModelProjectWizard_3;

	public PicosNewWizard() {
		super(wz_title, wz_desc); 
		//TODO: This should work on the message file of the baseclass???
		org.eclipse.cdt.internal.ui.newui.Messages.CDTMainWizardPage_DefaultProjectCategory = "Makefile project"; //$NON-NLS-1$
		org.eclipse.cdt.internal.ui.newui.Messages.CDTMainWizardPage_DefaultProjectType = "Hello World LPC8xx Project"; //$NON-NLS-1$
	}

	//	@Override
	//	public void init(IWorkbench workbench, IStructuredSelection selection) {
	//		// TODO Auto-generated method stub
	//
	//	}

	@Override
	public void addPages() {
		//super.addPages();
		//opportunity to specialize Page class, and override filter.
		fMainPage= new PicosMainWizardPage(CUIPlugin.getResourceString(PREFIX));
		fMainPage.setTitle(wz_title);
		fMainPage.setDescription(wz_desc);
		addPage(fMainPage);
	}



	@Override
	public String[] getNatures() {
		return new String[] { CProjectNature.C_NATURE_ID, CCProjectNature.CC_NATURE_ID };
	}

	@Override
	protected IProject continueCreation(IProject prj) {
		if (continueCreationMonitor == null) {
			continueCreationMonitor = new NullProgressMonitor();
		}

		try {
			continueCreationMonitor.beginTask(org.eclipse.cdt.internal.ui.newui.Messages.CCProjectWizard_0, 2); 
			CProjectNature.addCNature(prj, new SubProgressMonitor(continueCreationMonitor, 1));
			CCProjectNature.addCCNature(prj, new SubProgressMonitor(continueCreationMonitor, 1));
		} catch (CoreException e) {}
		finally {continueCreationMonitor.done();}
		return prj;
	}

	@Override
	public String[] getContentTypeIDs() {
		return new String[] { CCorePlugin.CONTENT_TYPE_CXXSOURCE, CCorePlugin.CONTENT_TYPE_CXXHEADER };
	}


	@Override
	public Image getDefaultPageImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean performFinish() {
		super.performFinish();
		try {
			createDebugLaunch(newProject);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void createDebugLaunch(IProject project) throws CoreException {
		String projectName = project.getName();
		String projectLocation = project.getLocation().toOSString();

		//ILaunchManager manager = new LaunchManager();
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = manager
				.getLaunchConfigurationType("org.eclipse.cdt.debug.gdbjtag.launchConfigurationType"); //$NON-NLS-1$
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(launchConfigurationType);
		for (int i = 0; i < configurations.length; i++) {
			ILaunchConfiguration configuration = configurations[i];
			if (configuration.getName().equals(projectName + Messages.PicosNewWizard_LaunchPostfix)) {
				configuration.delete();
				break;
			}
		}
		ILaunchConfigurationWorkingCopy workingCopy = launchConfigurationType
				.newInstance(project, projectName + Messages.PicosNewWizard_LaunchPostfix);
		Set<String> modes=new HashSet<String>();
		modes.add(Messages.PicosNewWizard_3);		

		//See org.eclipse.cdt.debug.gdbjtag.ui.GDBJtagStartupTab.java
		// Initialization Commands
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_DO_RESET, IGDBJtagConstants.DEFAULT_DO_RESET);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_DELAY, IGDBJtagConstants.DEFAULT_DELAY);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_DO_HALT, IGDBJtagConstants.DEFAULT_DO_HALT);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_INIT_COMMANDS, IGDBJtagConstants.DEFAULT_INIT_COMMANDS);
		// Load Image...
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_LOAD_IMAGE, IGDBJtagConstants.DEFAULT_LOAD_IMAGE);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_IMAGE, IGDBJtagConstants.DEFAULT_USE_PROJ_BINARY_FOR_IMAGE);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_USE_FILE_FOR_IMAGE, IGDBJtagConstants.DEFAULT_USE_FILE_FOR_IMAGE);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_IMAGE_FILE_NAME, IGDBJtagConstants.DEFAULT_IMAGE_FILE_NAME);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_IMAGE_OFFSET, IGDBJtagConstants.DEFAULT_IMAGE_OFFSET);
		//.. and Symbols
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_LOAD_SYMBOLS, IGDBJtagConstants.DEFAULT_LOAD_SYMBOLS);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_SYMBOLS, IGDBJtagConstants.DEFAULT_USE_PROJ_BINARY_FOR_SYMBOLS);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_USE_FILE_FOR_SYMBOLS, IGDBJtagConstants.DEFAULT_USE_FILE_FOR_SYMBOLS);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_SYMBOLS_FILE_NAME, IGDBJtagConstants.DEFAULT_SYMBOLS_FILE_NAME);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_SYMBOLS_OFFSET, IGDBJtagConstants.DEFAULT_SYMBOLS_OFFSET);

		// Runtime Options
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_SET_PC_REGISTER, IGDBJtagConstants.DEFAULT_SET_PC_REGISTER);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_PC_REGISTER, IGDBJtagConstants.DEFAULT_PC_REGISTER);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_SET_STOP_AT, IGDBJtagConstants.DEFAULT_SET_STOP_AT);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_STOP_AT, IGDBJtagConstants.DEFAULT_STOP_AT);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_SET_RESUME, IGDBJtagConstants.DEFAULT_SET_RESUME);
		// Run Commands
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_RUN_COMMANDS, Messages.PicosNewWizard_RunCommand); 

		//See org.eclipse.cdt.debug.gdbjtag.ui.GDBJtagDebuggerTab.java and GDBJtagDSFDebuggerTab.java
		//TODO: This is marked deprecated. What is the new approach?
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_JTAG_DEVICE, Messages.PicosNewWizard_JtagDevice);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_IP_ADDRESS, Messages.PicosNewWizard_IPAddress);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_PORT_NUMBER, 3333);

		//// From: org.eclipse.cdt.dsf.gdb.internal.ui.GdbPlugin.java: GdbPlugin.PLUGIN_ID
		//final String PLUGIN_ID = "org.eclipse.cdt.dsf.gdb.ui";	
		//String defaultGdbCommand = Platform.getPreferencesService().getString(PLUGIN_ID,
		//		IGdbDebugPreferenceConstants.PREF_DEFAULT_GDB_COMMAND, "", null);
		workingCopy.setAttribute(IMILaunchConfigurationConstants.ATTR_DEBUG_NAME, Messages.PicosNewWizard_GdbExecutable);
		workingCopy.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME, Messages.PicosNewWizard_GdbExecutable);

		CommandFactoryManager cfManager = MIPlugin.getDefault().getCommandFactoryManager();
		CommandFactoryDescriptor defDesc = cfManager.getDefaultDescriptor(IGDBJtagConstants.DEBUGGER_ID);
		workingCopy.setAttribute(IMILaunchConfigurationConstants.ATTR_DEBUGGER_COMMAND_FACTORY, defDesc.getName());
		workingCopy.setAttribute(IMILaunchConfigurationConstants.ATTR_DEBUGGER_PROTOCOL, defDesc.getMIVersions()[0]);
		workingCopy.setAttribute(IMILaunchConfigurationConstants.ATTR_DEBUGGER_VERBOSE_MODE,
				IMILaunchConfigurationConstants.DEBUGGER_VERBOSE_MODE_DEFAULT);
		workingCopy.setAttribute(IGDBJtagConstants.ATTR_USE_REMOTE_TARGET,
				IGDBJtagConstants.DEFAULT_USE_REMOTE_TARGET);
		workingCopy.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUGGER_UPDATE_THREADLIST_ON_SUSPEND,
				IGDBLaunchConfigurationConstants.DEBUGGER_UPDATE_THREADLIST_ON_SUSPEND_DEFAULT);


		//See org.eclipse.cdt.launch.ui.CAbstractMainTab.java
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_BUILD_BEFORE_LAUNCH, 
				ICDTLaunchConfigurationConstants.BUILD_BEFORE_LAUNCH_ENABLED);
		//See org.eclipse.cdt.launch.ui.CMainTab.java
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_COREFILE_PATH, ""); //$NON-NLS-1$

		////TODO: This is not present in initial creation, and related to display of CPU registers.
		////See org.eclipse.cdt.debug.internal.core.CRegisterManager.java
		//workingCopy.setAttribute( ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_REGISTER_GROUPS, "" /*getMemento()*/ );

		//See org.eclipse.cdt.launch.ui.CMainTab.java
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME, projectLocation+Messages.PicosNewWizard_FirmwarePath);
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
		// Set the auto choose build configuration to true for new configurations.
		// Existing configurations created before this setting was introduced will have this disabled.
		//TODO: Check should be false?
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_BUILD_CONFIG_AUTO, true); 
		//TODO: Check strange number in real case
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_BUILD_CONFIG_ID, ""); //$NON-NLS-1$

		//TODO: No code reference found to setting MAPPED_RESOURCE attributes
		ArrayList<String> listValue = new ArrayList<String>();
		listValue.add(new String("/"+projectName)); //$NON-NLS-1$
		workingCopy.setAttribute("org.eclipse.debug.core.MAPPED_RESOURCE_PATHS", listValue); //$NON-NLS-1$
		listValue = new ArrayList<String>();
		listValue.add(new String("4")); //$NON-NLS-1$
		workingCopy.setAttribute("org.eclipse.debug.core.MAPPED_RESOURCE_TYPES", listValue); //$NON-NLS-1$

		//See org.eclipse.cdt.launch.ui.CommonTabLite.java
		listValue = new ArrayList<String>();
		listValue.add(new String("org.eclipse.debug.ui.launchGroup.debug")); //$NON-NLS-1$
		workingCopy.setAttribute("org.eclipse.debug.ui.favoriteGroups", //$NON-NLS-1$
				listValue);

		ILaunchConfiguration configuration = workingCopy.doSave();
		// DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	}


}

