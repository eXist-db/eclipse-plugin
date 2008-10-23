/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Special implementation for the xquery language. This will use for
 * {@link IRule} implementations.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryWordDetector implements IWordDetector {

	public boolean isWordPart(char character) {
		return XQuerySyntaxUtils.isXQueryIdentifierPart(character);
	}

	public boolean isWordStart(char character) {
		return XQuerySyntaxUtils.isXQueryIdentifierStart(character);
	}

}
