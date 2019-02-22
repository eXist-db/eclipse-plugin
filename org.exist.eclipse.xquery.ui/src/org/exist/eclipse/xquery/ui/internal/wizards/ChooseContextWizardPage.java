package org.exist.eclipse.xquery.ui.internal.wizards;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
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
 * Select the type of the connection. This page will start the connection
 * wizard.
 * 
 * @author Pascal Schmidiger
 */
public class ChooseContextWizardPage extends WorkbenchWizardSelectionPage {

	private Map<String, IContextSwitcher> _switcher;
	private IContextSwitcher _selected;
	private TableViewer _viewer;

	public ChooseContextWizardPage(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		super("choosecontextwizardpage", aWorkbench, currentSelection, null, null);
		setTitle(SelectContextWizard.WIZARD_TITLE);
		setDescription("Select a connection.");
		setImageDescriptor(XQueryUI.getImageDescriptor("icons/existdb.png"));
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		_switcher = ContextSwitcherRegistration.getInstance().getContextSwitchersAsMap();

		_viewer = new TableViewer(container, SWT.FULL_SELECTION | SWT.BORDER);
		_viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		_viewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return _switcher.values().toArray();
			}
		});

		_viewer.setLabelProvider(new LabelProvider() {

			private Image _connectionImage;

			@Override
			public Image getImage(Object element) {
				return getConnectionImage();
			}

			@Override
			public void dispose() {
				super.dispose();
				if (_connectionImage != null) {
					_connectionImage.dispose();
				}
			}

			private Image getConnectionImage() {
				if (_connectionImage == null) {
					_connectionImage = XQueryUI.getImageDescriptor("icons/connection_open.png").createImage();
				}
				return _connectionImage;
			}

			@Override
			public String getText(Object element) {
				return ((IContextSwitcher) element).getName();
			}
		});
		_viewer.setComparator(new ViewerComparator());

		_viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (getWizard().performFinish()) {
					getWizard().getContainer().getShell().close();
				}
			}
		});
		_viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				_selected = (IContextSwitcher) ((IStructuredSelection) event.getSelection()).getFirstElement();
				setErrorMessage(null);
				setPageComplete(true);
				updateSelectedNode();
			}
		});

		// SWT.FULL_SELECTION does not have an effect. create effect with
		// TableColumn that fill's horizontally
		_viewer.getTable().addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				_viewer.getTable().getColumns()[0].setWidth(
						_viewer.getTable().getSize().x - (_viewer.getTable().getVerticalBar().getSize().x + 5));
			}
		});
		new TableColumn(_viewer.getTable(), SWT.NONE);

		setControl(container);

		_viewer.setInput(new Object());
		if (_viewer.getTable().getItemCount() == 0) {
			setErrorMessage("There exists no connection. A connection can be created in eXist Explorer.");
			setPageComplete(false);
		} else {

			IContextSwitcher newSel = null;
			String last = SelectContextWizard.getLastSelectedConnection();
			if (!last.isEmpty()) {
				for (IContextSwitcher it : _switcher.values()) {
					if (it.getName().equals(last)) {
						newSel = it;
						break;
					}
				}
			}

			if (newSel == null) {
				newSel = (IContextSwitcher) _viewer.getTable().getItem(0).getData();
			}
			_viewer.setSelection(new StructuredSelection(newSel));
		}
	}

	public final IContextSwitcher getSelected() {
		return _selected;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////

	private void updateSelectedNode() {
		setErrorMessage(null);
		IWizardDescriptor element = WorkbenchPlugin.getDefault().getNewWizardRegistry()
				.findWizard(SelectContextWizard.class.getCanonicalName());
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
				SelectContextWizard selectContextWizard = (SelectContextWizard) wizardElement.createWizard();
				return selectContextWizard;
			}
		};
	}
}
