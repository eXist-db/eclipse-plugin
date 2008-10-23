/**
 * 
 */
package org.exist.eclipse.auto.internal.exception;

/**
 * The AutoException represents an exception related to the automation.
 * 
 * @author Markus Tanner
 */
public class AutoException extends Exception {
	private static final long serialVersionUID = -1802981190711923270L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public AutoException() {
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 * @param message
	 * @param cause
	 */
	public AutoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 * @param message
	 */
	public AutoException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 * @param cause
	 */
	public AutoException(Throwable cause) {
		super(cause);
	}

}
