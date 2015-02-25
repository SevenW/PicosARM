package com.sevenwatt.picos.wizards;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.internal.ui.newui.Messages;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.wizards.CDTCommonProjectWizard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.swt.graphics.Image;

/**
 * The wizard to create new MBS C++ Project.
 */
public class PicosNewWizard extends CDTCommonProjectWizard {
	private static final String PREFIX= "CProjectWizard"; //$NON-NLS-1$
	private static final String wz_title = Messages.NewModelProjectWizard_2;
	private static final String wz_desc = Messages.NewModelProjectWizard_3;

	public PicosNewWizard() {
		super(wz_title, wz_desc); 
		Messages.CDTMainWizardPage_DefaultProjectCategory = "Makefile project"; //$NON-NLS-1$
		Messages.CDTMainWizardPage_DefaultProjectType = "Hello World LPC8xx Project"; //$NON-NLS-1$
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
			continueCreationMonitor.beginTask(Messages.CCProjectWizard_0, 2); 
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


}

