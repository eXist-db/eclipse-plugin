/**
 * DbTypeEnum.java
 */
package org.exist.eclipse;

/**
 * Enum of the connection types.
 * 
 * @author Pascal Schmidiger
 * 
 */
public enum ConnectionEnum {
	REMOTE, LOCAL;
	
	public boolean isRemote() {
		return REMOTE == this;
	}
	
	public static ConnectionEnum valueOfName(String name) {
		return valueOf(name.toUpperCase());
	}
}
