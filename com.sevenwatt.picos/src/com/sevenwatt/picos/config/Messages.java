package com.sevenwatt.picos.config;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sevenwatt.picos.config.messages"; //$NON-NLS-1$
	public static String ConfigDialog_GNUUtils;
	public static String ConfigDialog_MakeTool;
	public static String ConfigDialog_Message;
	public static String ConfigDialog_Title;
	public static String ConfigDialog_Toolchain;
	public static String ConfigHandler_ChainPattern;
	public static String ConfigHandler_Compiler_exe;
	public static String ConfigHandler_DefaultChain;
	public static String ConfigHandler_DefaultMake;
	public static String ConfigHandler_DefaultUtils;
	public static String ConfigHandler_fastSearch;
	public static String ConfigHandler_Make_exe;
	public static String ConfigHandler_MakePattern;
	public static String ConfigHandler_Mkdir_exe;
	public static String ConfigHandler_Search;
	public static String ConfigHandler_Std_Includes;
	public static String ConfigHandler_UtilsPattern;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
