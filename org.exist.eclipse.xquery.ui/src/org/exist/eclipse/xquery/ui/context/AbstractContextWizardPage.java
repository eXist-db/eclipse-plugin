/**
 * 
 */
package org.exist.eclipse.xquery.ui.context;

import org.eclipse.jface.wizard.WizardPage;

/**
 * This is the abstract wizardpage class, which is used to change the context on
 * the xquery editor. First of all you have to register a
 * {@link IContextSwitcher} on the singleton instance
 * {@link ContextSwitcherRegistration}. If this context will selected from the
 * user, then the system returns instances of this type. Here you can define
 * your own logic to choose the {@link IConnectionContext}, but at the end you
 * have to give back an {@link IConnectionContext} instance over the method
 * {@link AbstractContextWizardPage#getConnectionContext()}. Therefore you must
 * override this method, otherwise it will return <code>null</code> on default.
 * 
 * @see IContextSwitcher
 * @see ContextSwitcherRegistration
 * @see IConnectionContext
 * 
 * @author Pascal Schmidiger
 * 
 */
public abstract class AbstractContextWizardPage extends WizardPage {

	/**
	 * @param pageName
	 */
	protected AbstractContextWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @return the context which was choose from the user and will be set on the
	 *         xquery editor.
	 */
	public IConnectionContext getConnectionContext() {
		return null;
	}
}
