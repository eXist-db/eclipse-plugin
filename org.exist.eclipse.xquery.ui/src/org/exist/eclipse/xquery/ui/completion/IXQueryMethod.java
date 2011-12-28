/**
 * 
 */
package org.exist.eclipse.xquery.ui.completion;

/**
 * @author Pascal Schmidiger
 * 
 */
public interface IXQueryMethod {

	public int getFlags();

	public String[] getParameterNames();

	public String getName();

	public String[] getParameterTypes();
}