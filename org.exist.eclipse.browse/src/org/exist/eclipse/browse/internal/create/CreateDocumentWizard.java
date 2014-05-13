package org.exist.eclipse.browse.internal.create;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.create.CreateDocumentException;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.views.document.ActionGroupOpenDocument;
import org.exist.eclipse.browse.internal.views.document.ActionOpenDocument;

/**
 * This wizard creates a new document.
 * 
 * @author Pascal Schmidiger
 */
public class CreateDocumentWizard extends Wizard implements IWorkbenchWizard {
	private static final String IMG = "icons/hslu_exist_eclipse_logo.jpg";
	private static final String TITLE = "Create a document";
	private EnterDocumentWizardPage _enterDocumentPage;
	private final IBrowseItem _item;
	private IBrowseService _itemService;

	public CreateDocumentWizard(IBrowseItem item) {
		super();
		_item = item;
		_itemService = (IBrowseService) _item.getAdapter(IBrowseService.class);
		setWindowTitle(TITLE);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		SelectDocumentWizardPage page = new SelectDocumentWizardPage();
		page.setTitle(TITLE);
		page.setImageDescriptor(BrowsePlugin.getImageDescriptor(IMG));
		addPage(page);

		_enterDocumentPage = new EnterDocumentWizardPage(_item);
		_enterDocumentPage.setTitle(TITLE);
		_enterDocumentPage.setImageDescriptor(BrowsePlugin
				.getImageDescriptor(IMG));
		addPage(_enterDocumentPage);
	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() == _enterDocumentPage) {
			return _enterDocumentPage.isPageComplete();
		} else {
			return false;
		}
	}

	@Override
	public boolean performFinish() {
		boolean isFinished = true;
		if (IManagementService.class.cast(
				_item.getConnection().getAdapter(IManagementService.class))
				.check()) {
			if (_itemService.check()) {
				IDocumentItem documentItem = _enterDocumentPage
						.getDocumentItem();
				try {
					IDocumentService documentService = (IDocumentService) documentItem
							.getAdapter(IDocumentService.class);
					documentService.create(_enterDocumentPage
							.getDocumentProvider(), null);
					_itemService.refresh();

					IEditorDescriptor defaultEditor = ActionGroupOpenDocument
							.getDefaultEditor(documentItem);
					if (defaultEditor != null) {
						// open in editor
						ActionOpenDocument openAction = new ActionOpenDocument(
								defaultEditor.getId(), documentItem);
						openAction.run();
					}

				} catch (CreateDocumentException e) {
					isFinished = false;
					_enterDocumentPage
							.setErrorMessage("Failure while create document.");
				}
			}
		}
		return isFinished;
	}

	@Override
	public boolean performCancel() {
		return _itemService.check();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
