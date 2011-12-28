/**
 * IDocumentListener.java
 */
package org.exist.eclipse.browse.document;

import org.eclipse.ui.IWorkbenchPage;

/**
 * This is the interface for the extension point 'document'. <br />
 * 
 * The time flow of the method is:
 * <ol>
 * <li>call {@link #init(IWorkbenchPage)}</li>
 * <li>call {@link #actionPerformed(IDocumentItem))}</li>
 * </ol>
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IDocumentListener {
	/**
	 * You have the possibility to start a view or something else.
	 * 
	 * @param page
	 */
	public void init(IWorkbenchPage page);

	/**
	 * You gets the {@link IDocumentItem}, which was selected.
	 * 
	 * @param items
	 *            on which you will perform your action.
	 */
	public void actionPerformed(IDocumentItem[] items);
}
