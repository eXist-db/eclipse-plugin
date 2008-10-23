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

	public String[] getParameters();

	public String getName();

}