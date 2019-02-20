/**
 * 
 */
package org.exist.eclipse.auto.internal.result.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.result.control.ResultContentProvider;
import org.exist.eclipse.auto.internal.result.control.ResultLabelProvider;
import org.exist.eclipse.auto.internal.result.editor.ResultFormPage;
import org.exist.eclipse.auto.internal.result.model.IResultModel;
import org.exist.eclipse.auto.internal.result.model.QueryResultEntity;

/**
 * This is the master block of the result editor.
 * 
 * @author Markus Tanner
 */
public class ResultBlock extends MasterDetailsBlock {

	ResultFormPage _page;
	ResultContentProvider _contentProvider;
	IResultModel _resultModel;
	Text _threadCount;

	/**
	 * ResultBlock constructor
	 * 
	 * @param page
	 * @param contentProvider
	 * @param resultModel
	 */
	public ResultBlock(ResultFormPage page, ResultContentProvider contentProvider, IResultModel resultModel) {
		_page = page;
		_contentProvider = contentProvider;
		_resultModel = resultModel;
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {

		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(gd);

		// Create a composite as container
		Composite navigation = toolkit.createComposite(parent);
		GridLayout navLayout = new GridLayout();
		navLayout.numColumns = 1;
		navigation.setLayout(navLayout);
		gd = new GridData(GridData.FILL_BOTH);
		navigation.setLayoutData(gd);

		// Queries Section
		ResultSelectionSection resultSelectionSection = new ResultSelectionSection(navigation, toolkit);
		Section querySection = resultSelectionSection.init();

		final SectionPart spart = new SectionPart(querySection);
		managedForm.addPart(spart);

		TableViewer _viewer = resultSelectionSection.getTableViewer();
		_viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});

		// Set the content for the table viewer
		_viewer.setContentProvider(_contentProvider);
		_viewer.setLabelProvider(new ResultLabelProvider());
		IEditorInput input = _page.getEditor().getEditorInput();
		_viewer.setInput(input);

	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// No toolbar actions
	}

	@Override
	protected void registerPages(DetailsPart details) {
		details.registerPage(QueryResultEntity.class, new QueryRunsSection());
	}

}
