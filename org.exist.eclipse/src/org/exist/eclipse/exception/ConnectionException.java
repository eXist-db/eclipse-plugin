package org.exist.eclipse.exception;

/**
 * Exception for Connection.
 * 
 * @author Markus Tanner
 */
public class ConnectionException extends RuntimeException {
	private static final long serialVersionUID = -1802981190711923270L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public ConnectionException() {
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 * @param message
	 * @param cause
	 */
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 * @param message
	 */
	public ConnectionException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 * @param cause
	 */
	public ConnectionException(Throwable cause) {
		super(cause);
	}
}
