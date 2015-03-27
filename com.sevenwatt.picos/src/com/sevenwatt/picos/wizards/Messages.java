package com.sevenwatt.picos.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sevenwatt.picos.wizards.messages"; //$NON-NLS-1$
	public static String PicosMainWizardPage_templatePathKeyword;
	public static String PicosNewWizard_3;
	public static String PicosNewWizard_FirmwarePath;
	public static String PicosNewWizard_GdbExecutable;
	public static String PicosNewWizard_IPAddress;
	public static String PicosNewWizard_JtagDevice;
	public static String PicosNewWizard_LaunchPostfix;
	public static String PicosNewWizard_RunCommand;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
