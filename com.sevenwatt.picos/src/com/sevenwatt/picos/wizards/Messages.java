package com.sevenwatt.picos.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sevenwatt.picos.wizards.messages"; //$NON-NLS-1$
	public static String PicosMainWizardPage_templatePathKeyword;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
