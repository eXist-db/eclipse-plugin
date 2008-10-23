/**
 * 
 */
package org.exist.eclipse.xquery.ui.completion;

/**
 * @author Pascal Schmidiger
 * 
 */
public interface ICompletionExtension {
	public IXQueryMethod[] getMethods(String prefix);
}
