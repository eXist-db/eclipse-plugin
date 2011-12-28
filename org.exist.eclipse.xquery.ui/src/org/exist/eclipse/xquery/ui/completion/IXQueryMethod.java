/**
 * 
 */
package org.exist.eclipse.xquery.ui.completion;

/**
 * @author Pascal Schmidiger
 * 
 */
public interface IXQueryMethod {

	String MORE = "...";

	int getFlags();

	String[] getParameterNames();

	String getName();

	String[] getParameterTypes();

	String getComment();

	String getSignature();
}