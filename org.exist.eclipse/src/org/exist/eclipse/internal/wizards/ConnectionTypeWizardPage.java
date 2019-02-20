package org.exist.eclipse.internal.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardNode;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardSelectionPage;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.exist.eclipse.ConnectionEnum;
import org.exist.eclipse.internal.BasePlugin;

/**
 * Select the type of the connection. This page will start the
 * {@link RemoteConnectionWizard} or the {@link LocalConnectionWizard}.
 * 
 * @author Pascal Schmidiger
 */
public class ConnectionTypeWizardPage extends WorkbenchWizardSelectionPage {
	private ConnectionEnum _type;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param aWorkbench       the workbench instance
	 * @param currentSelection the current selection
	 */
	public ConnectionTypeWizardPage(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		super("connectiontypewizardpage", aWorkbench, currentSelection, null, null);
		setTitle(NewConnectionWizard.WIZARD_TITLE);
		setDescription("Select the type of the connection");
		setImageDescriptor(BasePlugin.getImageDescriptor("icons/existdb.png"));
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		Button button = null;
		for (ConnectionEnum type : ConnectionEnum.values()) {
			button = new Button(container, SWT.RADIO);
			String typeName = type.name();

			// ui elements should start with an uppercase letter
			typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);

			button.setText(typeName);
			button.setData(ConnectionEnum.class.toString(), type);

			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					_type = (ConnectionEnum) e.widget.getData(ConnectionEnum.class.toString());
					setErrorMessage(null);
					setPageComplete(true);
					updateSelectedNode();
				}
			});
		}

		setControl(container);
		setErrorMessage("Select a type");
		setPageComplete(false);
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	private ConnectionEnum getType() {
		return _type;
	}

	private void updateSelectedNode() {
		setErrorMessage(null);
		IWizardDescriptor element = null;
		if (ConnectionEnum.REMOTE.equals(getType())) {
			element = WorkbenchPlugin.getDefault().getNewWizardRegistry()
					.findWizard(RemoteConnectionWizard.class.getCanonicalName());
		} else if (ConnectionEnum.LOCAL.equals(getType())) {
			element = WorkbenchPlugin.getDefault().getNewWizardRegistry()
					.findWizard(LocalConnectionWizard.class.getCanonicalName());
			setSelectedNode(createWizardNode(element));
		}

		if (element == null) {
			setSelectedNode(null);
			return;
		}
		setSelectedNode(createWizardNode(element));
		setMessage(element.getDescription());
	}

	/*
	 * Create a wizard node given a wizard's descriptor.
	 */
	private IWizardNode createWizardNode(IWizardDescriptor element) {
		return new WorkbenchWizardNode(this, element) {
			@Override
			public IWorkbenchWizard createWizard() throws CoreException {
				return wizardElement.createWizard();
			}
		};
	}
}
