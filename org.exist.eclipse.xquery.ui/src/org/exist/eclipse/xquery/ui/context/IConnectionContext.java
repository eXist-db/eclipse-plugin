/**
 * 
 */
package org.exist.eclipse.xquery.ui.context;

import org.exist.eclipse.xquery.ui.completion.ICompletionExtension;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * This is the connection context, which could set in the xquery editor and will
 * used for running xqueries.
 * 
 * @see AbstractContextWizardPage
 * @see IContextSwitcher
 * 
 * @author Pascal Schmidiger
 */
public interface IConnectionContext {

	/**
	 * Register the listener, if you want information about changes on the context.
	 * 
	 * @param listener
	 */
	public void addContextListener(IContextListener listener);

	/**
	 * Remove the given <code>listener</code>.
	 * 
	 * @param listener
	 */
	public void removeContextListener(IContextListener listener);

	/**
	 * @return a name of the context, which is shown in the xquery editor as
	 *         identification.
	 */
	public String getName();

	/**
	 * This method will call, when a query should run in this context. Attention:
	 * This method should not blocking. Therefore implement the running in a
	 * separate job.
	 * 
	 * @param frame
	 */
	public void run(IQueryFrame frame);

	/**
	 * @return the {@link ICompletionExtension}, which is used to extend the code
	 *         assistant with this context.
	 */
	public ICompletionExtension getCompletionExtension();
}
