package com.sevenwatt.picos.config;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.envvar.IContributedEnvironment;
import org.eclipse.cdt.core.envvar.IEnvironmentVariable;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConfigDialog extends TitleAreaDialog {

	private Text buildToolsText;
	private Text toolchainText;
	private Text gnuUtilsText;

	private String buildTools = ""; //$NON-NLS-1$
	private String toolchain = ""; //$NON-NLS-1$
	private String gnuUtils = ""; //$NON-NLS-1$

	public ConfigDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.ConfigDialog_Title);
		setMessage(Messages.ConfigDialog_Message, IMessageProvider.INFORMATION);
		buildToolsText.setText(buildTools);
		toolchainText.setText(toolchain);
		gnuUtilsText.setText(gnuUtils);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createBuildTools(container);
		createToolchain(container);
		createGNUUtils(container);
		addSeparator(parent);

		return area;
	}


	
	private void createBuildTools(Composite container) {
		Label lbtBuildTools = new Label(container, SWT.NONE);
		lbtBuildTools.setText(Messages.ConfigDialog_MakeTool);

		GridData dataBuildTools = new GridData();
		dataBuildTools.grabExcessHorizontalSpace = true;
		dataBuildTools.horizontalAlignment = GridData.FILL;
		buildToolsText = new Text(container, SWT.BORDER);
		buildToolsText.setLayoutData(dataBuildTools);
	}

	private void createToolchain(Composite container) {
		Label lbtToolchain = new Label(container, SWT.NONE);
		lbtToolchain.setText(Messages.ConfigDialog_Toolchain);

		GridData dataToolchain = new GridData();
		dataToolchain.grabExcessHorizontalSpace = true;
		dataToolchain.horizontalAlignment = GridData.FILL;
		toolchainText = new Text(container, SWT.BORDER);
		toolchainText.setLayoutData(dataToolchain);
	}

	private void createGNUUtils(Composite container) {
		Label lbtGNUUtils = new Label(container, SWT.NONE);
		lbtGNUUtils.setText(Messages.ConfigDialog_GNUUtils);

		GridData dataGNUUtils = new GridData();
		dataGNUUtils.grabExcessHorizontalSpace = true;
		dataGNUUtils.horizontalAlignment = GridData.FILL;
		gnuUtilsText = new Text(container, SWT.BORDER);
		gnuUtilsText.setLayoutData(dataGNUUtils);
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		buildTools = buildToolsText.getText();
		toolchain = toolchainText.getText();
		gnuUtils = gnuUtilsText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getBuildTools() {
		return buildTools;
	}
	
	public void setBuildTools(String bTools) {
		if (bTools == null)
			buildTools = ""; //$NON-NLS-1$
		else
			buildTools = bTools;
	}

	public String getToolchain() {
		return toolchain;
	}

	public void setToolchain(String tChain) {
		if (tChain == null)
			toolchain = ""; //$NON-NLS-1$
		else
			toolchain = tChain;
	}

	public String getGNUUtils() {
		return gnuUtils;
	}

	public void setGNUUtils(String gUtils) {
		if (gUtils == null)
			gnuUtils = ""; //$NON-NLS-1$
		else
			gnuUtils = gUtils;
	}
} 