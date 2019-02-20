package org.exist.eclipse;

public interface ICredentials {

	/**
	 * @returns the user name of the current connection.
	 */
	public String getUsername();

	/**
	 * @returns the password of the current connection.
	 */
	public String getPassword();

}
