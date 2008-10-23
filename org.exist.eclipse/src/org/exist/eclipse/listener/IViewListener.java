/**
 * IViewListener.java
 */
package org.exist.eclipse.listener;

import org.eclipse.ui.IWorkbenchPage;

/**
 * Implement this, when you will open a view, if a connection is established.
 * 
 * @see ViewRegistration
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IViewListener {
	/**
	 * The method is called, if a connection is established.
	 * 
	 * @param page
	 *            open your view with this page
	 */
	public void openView(IWorkbenchPage page);
}
