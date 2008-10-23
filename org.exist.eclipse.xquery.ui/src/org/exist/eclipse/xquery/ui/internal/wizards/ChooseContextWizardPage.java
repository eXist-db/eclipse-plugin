package org.exist.eclipse.xquery.ui.internal.wizards;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardNode;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardSelectionPage;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.context.ContextSwitcherRegistration;
import org.exist.eclipse.xquery.ui.context.IContextSwitcher;

/**
 * Select the type of the connection. This page will start the
 * {@link RemoteConnectionWizard} or the {@link LocalConnectionWizard}.
 * 
 * @author Pascal Schmidiger
 */
@SuppressWarnings("restriction")
public class ChooseContextWizardPage extends WorkbenchWizardSelectionPage {

	private Map<String, IContextSwitcher> _switcher;
	private IContextSwitcher _selected;

	public ChooseContextWizardPage(IWorkbench aWorkbench,
			IStructuredSelection currentSelection) {
		super("choosecontextwizardpage", aWorkbench, currentSelection, null,
				null);
		setTitle(SelectContextWizard.WIZARD_TITLE);
		setDescription("Select the context.");
		setImageDescriptor(XQueryUI
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		_switcher = ContextSwitcherRegistration.getInstance()
				.getContextSwitchersAsMap();

		Button button = null;
		for (String name : _switcher.keySet()) {
			button = new Button(container, SWT.RADIO);
			button.setText(name);
			button.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					_selected = _switcher.get(Button.class.cast(e.getSource())
							.getText());
					setErrorMessage(null);
					setPageComplete(true);
					updateSelectedNode();
				}
			});
		}

		setControl(container);
		setErrorMessage("Select a context");
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		if (_selected != null && _selected.getWizardPages() != null
				&& _selected.getWizardPages().length < 1) {
			return false;
		}
		return super.canFlipToNextPage();
	}

	public final IContextSwitcher getSelected() {
		return _selected;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////

	private void updateSelectedNode() {
		setErrorMessage(null);
		IWizardDescriptor element = WorkbenchPlugin.getDefault()
				.getNewWizardRegistry().findWizard(
						SelectContextWizard.class.getCanonicalName());
		setSelectedNode(createWizardNode(element));
		setMessage(element.getDescription());
	}

	/*
	 * Create a wizard node given a wizard's descriptor.
	 */
	private IWizardNode createWizardNode(IWizardDescriptor element) {
		return new WorkbenchWizardNode(this, element) {
			public IWorkbenchWizard createWizard() throws CoreException {
				SelectContextWizard selectContextWizard = (SelectContextWizard) wizardElement
						.createWizard();
				selectContextWizard.setPages(_selected.getWizardPages());
				return selectContextWizard;
			}
		};
	}
}
