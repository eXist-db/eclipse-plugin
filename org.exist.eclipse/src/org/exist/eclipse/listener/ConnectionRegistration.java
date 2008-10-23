/**
 * ConnectionRegistration.java
 */
package org.exist.eclipse.listener;

import org.exist.eclipse.internal.ConnectionBox;

/**
 * Here you can registered your {@link IConnectionListener}.
 * 
 * @see IConnectionListener
 * @author Pascal Schmidiger
 * 
 */
public class ConnectionRegistration {

	/**
	 * Add a new listener.
	 * 
	 * @param listener
	 */
	public static void addListener(IConnectionListener listener) {
		ConnectionBox.getInstance().addListener(listener);
	}

	/**
	 * Remove an unused listener.
	 * 
	 * @param listener
	 */
	public static void removeListener(IConnectionListener listener) {
		ConnectionBox.getInstance().removeListener(listener);
	}
}
