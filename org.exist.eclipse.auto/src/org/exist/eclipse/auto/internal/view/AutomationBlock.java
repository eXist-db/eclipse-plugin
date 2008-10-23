/**
 * 
 */
package org.exist.eclipse.auto.internal.view;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.control.AutoContentProvider;
import org.exist.eclipse.auto.internal.control.AutoLabelProvider;
import org.exist.eclipse.auto.internal.editor.AutomationFormPage;
import org.exist.eclipse.auto.internal.mod.AutoModEvent;
import org.exist.eclipse.auto.internal.mod.IAutoModificationListener;
import org.exist.eclipse.auto.internal.mod.IAutoModificationNotifier;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.model.QueryEntity;

/**
 * AutomationBlock represents the master part of the automation configuration.
 * It contains the modification part of the automation attributes and the query
 * selection part.
 * 
 * @author Markus Tanner
 */
public class AutomationBlock extends MasterDetailsBlock implements
		IAutoModificationNotifier {
	private AutomationFormPage _page;
	private AutoContentProvider _contentProvider;
	private IAutoModel _autoModel;
	private Collection<IAutoModificationListener> _modListeners = new ArrayList<IAutoModificationListener>();
	private TableViewer _viewer;

	/**
	 * Constructor
	 * 
	 * @param page
	 * @param contentProvider
	 * @param autoModel
	 */
	public AutomationBlock(AutomationFormPage page,
			AutoContentProvider contentProvider, IAutoModel autoModel) {
		_page = page;
		_contentProvider = contentProvider;
		_autoModel = autoModel;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		Composite navigation = toolkit.createComposite(parent);
		GridLayout navLayout = new GridLayout();
		navLayout.numColumns = 1;
		navigation.setLayout(navLayout);
		gd = new GridData(GridData.FILL_BOTH);
		navigation.setLayoutData(gd);

		// Automation Section
		AutomationSection autoSection = new AutomationSection(navigation,
				_autoModel, toolkit, this);
		autoSection.init();
		createSpacer(toolkit, navigation, 1);

		// Queries Section
		QueriesSection queriesSection = new QueriesSection(navigation,
				_autoModel, toolkit, this);
		Section querySection = queriesSection.init();

		final SectionPart spart = new SectionPart(querySection);
		managedForm.addPart(spart);

		_viewer = queriesSection.getTableViewer();
		_viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});

		_viewer.setContentProvider(_contentProvider);
		_viewer.setLabelProvider(new AutoLabelProvider());
		_viewer.setInput(_page.getEditor().getEditorInput());

		modificationCleared(new AutoModEvent("Modification cleared"));
	}

	/**
	 * Add the automation modification listener
	 * 
	 * @param listener
	 */
	public void addModificationListener(IAutoModificationListener listener) {
		_modListeners.add(listener);
	}

	/**
	 * Updates the query result content.
	 * 
	 * @param event
	 */
	public void automationModified(AutoModEvent event) {
		for (IAutoModificationListener listener : _modListeners) {
			listener.automationModified(event);
		}
		if (_viewer != null) {
			_viewer.refresh();
		}
	}

	/**
	 * Cleares the automation modification.
	 * 
	 * @param event
	 */
	public void modificationCleared(AutoModEvent event) {
		for (IAutoModificationListener listener : _modListeners) {
			listener.modificationCleared(event);
		}
	}

	//--------------------------------------------------------------------------
	// Actions
	//--------------------------------------------------------------------------

	protected void createToolBarActions(IManagedForm managedForm) {

		ToolBarActions toolBarActions = new ToolBarActions(managedForm,
				sashForm, _autoModel);
		toolBarActions.create();
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(QueryEntity.class, new QueryDetailsSection(
				this));
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

	/**
	 * Creates a given space between controls.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param span
	 */
	private void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, "");
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}
}
