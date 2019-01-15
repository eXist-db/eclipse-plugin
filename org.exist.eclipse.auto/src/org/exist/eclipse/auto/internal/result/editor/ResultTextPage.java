/**
 * 
 */
package org.exist.eclipse.auto.internal.result.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the text page of the result editor. Over this TextEditor the result
 * is loaded.
 * 
 * @author Markus Tanner
 */
public class ResultTextPage extends TextEditor implements IFormPage {

	private ResultEditor _editor;
	private int _index;
	private Control _partControl;

	/**
	 * ResultTextPage constructor
	 */
	public ResultTextPage(ResultEditor editor) {
		initialize(editor);
	}

	/**
	 * Sets the content of this text editor to the given new content.
	 */
	protected void setContent(String newContent) {
		getDocument().set(newContent);
	}

	/**
	 * @return The IDocument of this text editor.
	 */
	private IDocument getDocument() {
		return getDocumentProvider().getDocument(getEditorInput());
	}

	/**
	 * @return The content of the editor
	 */
	protected String getContent() {
		return getDocumentProvider().getDocument(getEditorInput()).get();
	}

	@Override
	public boolean canLeaveThePage() {
		return true;
	}

	@Override
	public FormEditor getEditor() {
		return _editor;
	}

	@Override
	public String getId() {
		return _editor.getSite().getId() + ".resultTextPage";
	}

	@Override
	public int getIndex() {
		return _index;
	}

	@Override
	public IManagedForm getManagedForm() {
		return null;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		_partControl = children[children.length - 1];
	}

	@Override
	public Control getPartControl() {
		return _partControl;
	}

	@Override
	public void initialize(FormEditor editor) {
		_editor = (ResultEditor) editor;
	}

	@Override
	public boolean isActive() {
		return equals(_editor.getActivePageInstance());
	}

	@Override
	public boolean isEditor() {
		return true;
	}

	@Override
	public boolean selectReveal(Object object) {
		return false;
	}

	@Override
	public void setActive(boolean active) {
	}

	@Override
	public void setIndex(int index) {
		_index = index;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

}
