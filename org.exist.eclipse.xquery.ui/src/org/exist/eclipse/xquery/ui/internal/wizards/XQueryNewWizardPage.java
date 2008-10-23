/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Wizard page for input the file name of a XQuery file.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryNewWizardPage extends WizardNewFileCreationPage {

	/**
	 * @param pageName
	 * @param selection
	 */
	public XQueryNewWizardPage(IStructuredSelection selection) {
		super("xquerynewwizardPage", selection);
		setDescription("This wizard creates a new file with *.*.xquery extension that can be opened by a xquery editor.");
		setFileName("new_file.xq");
	}

	/**
	 * Create a file for the entered name.
	 * 
	 * @return <code>true</code> if the file was created.
	 */
	public boolean finish() {
		createNewFile();
		return true;
	}

	@Override
	protected InputStream getInitialContents() {
		StringBuilder input = new StringBuilder();
		input.append("(:\n");
		input.append(getFileName()).append("\n");
		input.append(":)\n");
		input.append("xquery version \"1.0\";\n\n");
		input.append("query");
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
