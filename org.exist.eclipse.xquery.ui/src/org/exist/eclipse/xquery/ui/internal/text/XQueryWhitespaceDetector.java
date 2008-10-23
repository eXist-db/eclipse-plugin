/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Detects whitespace sign.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryWhitespaceDetector implements IWhitespaceDetector {
	public boolean isWhitespace(char character) {
		return Character.isWhitespace(character);
	}

}
