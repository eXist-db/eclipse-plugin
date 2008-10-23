/**
 * 
 */
package org.exist.eclipse.auto.internal.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.exist.eclipse.auto.data.Automation;

/**
 * On this WizardPage the Query context can be selected.
 * 
 * @author Markus Tanner
 */
public class AutomationNewWizardPage extends WizardNewFileCreationPage {

	/**
	 * @param pageName
	 * @param selection
	 */
	public AutomationNewWizardPage(IStructuredSelection selection) {
		super("xquerynewwizardPage", selection);
		setDescription("This wizard creates a new file with the ending '.eqa' so that it can be opened in the automation editor.");
		setFileName("new_automation.eqa");
	}

	/**
	 * Creates the new file
	 * 
	 * @return
	 */
	public boolean finish() {
		createNewFile();
		return true;
	}

	@Override
	protected InputStream getInitialContents() {
		try {
			return new ByteArrayInputStream(Automation
					.createEmptyAutomationXml().getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	protected String getNewFileLabel() {
		return "Automation name";
	}

}
