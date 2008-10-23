/**
 * 
 */
package org.exist.eclipse.auto.internal.mod;

/**
 * This class contains a automation modification event.
 * 
 * @author Markus Tanner
 */
public class AutoModEvent {

	private final String _modMessage;

	/**
	 * Create new modification event
	 * 
	 * @param modMessage
	 */
	public AutoModEvent(String modMessage) {
		_modMessage = modMessage;
	}

	/**
	 * Returns the modification message
	 * 
	 * @return Modification message
	 */
	public final String getModMessage() {
		return _modMessage;
	}
}
