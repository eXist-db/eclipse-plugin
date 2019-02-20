/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.text;

/**
 * Helper class for xquery language. It is like "isJavaIdentifier".
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySyntaxUtils {
	public static boolean isXQueryIdentifierPart(char character) {
		return Character.isJavaIdentifierPart(character) || character == ':' || character == '-';
	}

	public static boolean isXQueryIdentifierStart(char character) {
		return Character.isJavaIdentifierStart(character) || character == '$';
	}
}
