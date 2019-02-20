/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Wizard page for input the file name of a XQuery file.
 * 
 * @author Pascal Schmidiger
 */
public class NewXQueryFileWizardPage extends WizardNewFileCreationPage {

	private IFile _newFile;

	/**
	 * @param selection
	 */
	public NewXQueryFileWizardPage(IStructuredSelection selection) {
		super("xquerynewwizardPage", selection);
		setDescription(
				"This wizard creates a new file with *.*.xquery extension that can be opened by a XQuery editor.");
		setFileName("new_file.xq");
	}

	/**
	 * Create a file for the entered name.
	 * 
	 * @return <code>true</code> if the file was created.
	 */
	public boolean finish() {
		setNewFile(createNewFile());
		return true;
	}

	public IFile getNewFile() {
		return _newFile;
	}

	protected void setNewFile(IFile newFile) {
		_newFile = newFile;
	}

	@Override
	protected InputStream getInitialContents() {
		StringBuilder input = new StringBuilder();
		input.append("(:\n");
		input.append(getFileName()).append('\n');
		input.append(":)\n");
		input.append("xquery version \"1.0\";\n\n");
		input.append("//*");
		try {
			return new ByteArrayInputStream(input.toString().getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	protected String getNewFileLabel() {
		return "XQuery name";
	}

}
