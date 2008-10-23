/**
 * 
 */
package org.exist.eclipse.auto.internal.mod;

/**
 * This interface holds the ModificationListeners
 * 
 * @author Markus Tanner
 */
public interface IAutoModificationNotifier {

	/**
	 * Adds the modification listeners
	 * 
	 * @param listener
	 */
	public void addModificationListener(IAutoModificationListener listener);

	/**
	 * A modification happened and the listeners should be informed.
	 * 
	 * @param event
	 */
	public void automationModified(AutoModEvent event);

	/**
	 * Notify if the modification was cleared.
	 * 
	 * @param event
	 */
	public void modificationCleared(AutoModEvent event);
}
