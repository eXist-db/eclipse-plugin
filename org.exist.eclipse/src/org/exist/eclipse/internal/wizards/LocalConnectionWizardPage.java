package org.exist.eclipse.internal.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.internal.BasePlugin;
import org.exist.eclipse.internal.ConnectionBox;
import org.exist.eclipse.internal.LocalConnection;

/**
 * With this wizard you can add a new or change an existing local connection.
 * The user can define the configuration file, the username and the password.
 * Configuration file and username are mandatory fields.
 * 
 * @author Pascal Schmidiger
 */
public class LocalConnectionWizardPage extends WizardPage {

	public static void copy(final InputStream in, final OutputStream out) {
		try {
			final byte[] bytes = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(bytes)) > -1) {
				out.write(bytes, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
			try {
				out.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = "";

	private Text _name;
	private Text _username;
	private Text _password;
	private Text _config;
	private Button _browseBtn;
	private IConnection _connection;
	private boolean _copy;
	private Link _createDefaultConfigLink;

	/**
	 * Constructor for SampleNewWizardPage.
	 */
	public LocalConnectionWizardPage() {
		super("localconnectionwizardpage");
		setImageDescriptor(BasePlugin
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
		_copy = true;
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		// username
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText("&Name:");
		_name = new Text(container, SWT.BORDER | SWT.SINGLE);
		if (_connection != null) {
			_name.setText(_connection.getName());
		}

		// username
		Label userLabel = new Label(container, SWT.NULL);
		userLabel.setText("&Username:");
		_username = new Text(container, SWT.BORDER | SWT.SINGLE);
		if (_connection != null) {
			_username.setText(_connection.getUsername());
		} else {
			_username.setText(DEFAULT_USER);
		}

		// password
		Label passwordLabel = new Label(container, SWT.NULL);
		passwordLabel.setText("&Password:");
		_password = new Text(container, SWT.BORDER | SWT.SINGLE);
		_password.setEchoChar('*');
		if (_connection != null) {
			_password.setText(_connection.getPassword());
		} else {
			_password.setText(DEFAULT_PASSWORD);
		}

		// configuration
		Label configLabel = new Label(container, SWT.NULL);
		configLabel.setText("&Configuration:");
		_config = new Text(container, SWT.BORDER | SWT.SINGLE);
		if (_connection != null) {
			_config.setText(_connection.getPath());
		}
		_browseBtn = new Button(container, SWT.PUSH);
		_browseBtn.setText("Browse...");

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_name.setLayoutData(gd);
		_name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_username.setLayoutData(gd);
		_username.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_password.setLayoutData(gd);
		_password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		_config.setLayoutData(gd);
		_config.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		gd = new GridData(GridData.END);
		gd.horizontalSpan = 1;
		_browseBtn.setLayoutData(gd);
		_browseBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String config = selectConfigFile();
				if (config != null) {
					_config.setText(config);
				}
			}
		});

		new Label(container, 0);

		_createDefaultConfigLink = new Link(container, SWT.NONE);
		_createDefaultConfigLink.setText("<a>Create Default Configuration</a>");
		_createDefaultConfigLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCreateDefaultConfiguration(e);
			}
		});
		_createDefaultConfigLink.setVisible(false);
		gd = new GridData(0);

		gd.horizontalIndent = 10;
		_createDefaultConfigLink.setLayoutData(gd);

		_name.setFocus();

		setControl(container);
		setPageComplete(false);
		dialogChanged();
	}

	protected void onCreateDefaultConfiguration(SelectionEvent e) {
		File file = (File) _createDefaultConfigLink.getData(File.class
				.getName());
		if (file != null) {
			boolean ok = file.exists();
			if (!ok) {
				try {
					File parent = file.getParentFile();
					if (!parent.exists() && !parent.mkdirs()) {
						throw new RuntimeException("Folder '" + parent
								+ "' cannot be created.");
					}

					// needs 'data' folder
					File data = new File(parent, "data");
					if (!data.exists() && !data.mkdirs()) {
						throw new RuntimeException("Folder '" + data
								+ "' cannot be created.");
					}

					copy(LocalConnectionWizardPage.class
							.getResourceAsStream("conf.xml.dat"),
							new FileOutputStream(file));
					ok = true;
				} catch (Exception ex) {
					MessageDialog.openError(getShell(),
							"Create Default Configuration",
							"An error occured while creating the default configuration: "
									+ ex);
				}
			}

			if (ok) {
				_config.setText(file.getAbsolutePath());
			}
		}
	}

	protected IConnection getConnection() {
		return new LocalConnection(getConnectionName(), getUserName(),
				getPassword(), getUri());
	}

	protected void setConnection(IConnection connection, boolean copy) {
		_connection = connection;
		_copy = copy;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	private String getConnectionName() {
		return _name.getText();
	}

	private String getUri() {
		return _config.getText();
	}

	private String getUserName() {
		return _username.getText();
	}

	private String getPassword() {
		return _password.getText();
	}

	/**
	 * Opens a FileDialog so that the configuration file can be selected.
	 * Returns the path of the selected file. The return-value is empty in case
	 * the selection is not valid.
	 * 
	 * @return path of the selected file
	 */
	private String selectConfigFile() {
		FileDialog dialog = new FileDialog(getControl().getShell(), SWT.OPEN);
		dialog.setText("Select a Configuration File");
		String[] filterExtensions = { "*.xml", "*.*" };
		String[] filterNames = { "XML Files (*.xml)", "All Files (*.*)" };
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterNames(filterNames);
		return dialog.open();
	}

	/**
	 * Ensures that all text fields are set. Enable and disable fields according
	 * to the selection of the database type.
	 */
	private void dialogChanged() {
		String err = null;
		File createConfig = null;
		if (getConnectionName().length() < 1) {
			err = "Name must be specified.";
		} else if (!ConnectionBox.getInstance().isUnique(getConnectionName())) {
			if (!(!_copy && _connection != null && _connection.getName()
					.equals(getConnectionName()))) {
				err = "There exists already a connection with the same name.";
			}
		} else if (getUserName().length() < 1) {
			err = "Username must be specified.";
		} else {
			if (getUri().length() < 1) {
				err = "Browse for an existing configuration or enter a path to create a new one.";
			} else {
				File file = new File(getUri());
				if (!file.exists()) {
					err = "File '" + file + "' does not exist.";
					createConfig = new File(file, "conf.xml");
				} else if (!file.isFile()) {
					err = "File '" + file + "' is a folder, not a file.";
					createConfig = new File(file, "conf.xml");
				}
			}
		}
		setErrorState(err);
		_createDefaultConfigLink.setVisible(createConfig != null);
		if (createConfig != null) {
			String action = (createConfig.exists()) ? "Use"
					: "Create default configuration";
			_createDefaultConfigLink.setText("<a>" + action + " '"
					+ createConfig + "'</a>");
			_createDefaultConfigLink.getParent().layout();
			_createDefaultConfigLink
					.setData(File.class.getName(), createConfig);
		}
	}

	private void setErrorState(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}
