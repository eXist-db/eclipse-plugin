package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.dltk.ui.text.DLTKColorConstants;

/**
 * All different group of highlighting constants.
 * 
 * @author Pascal Schmidiger
 */
public interface IXQueryColorConstants {
	public static final String XQUERY_STRING = DLTKColorConstants.DLTK_STRING; // $NON-NLS-1$
	public static final String XQUERY_COMMENT = DLTKColorConstants.DLTK_MULTI_LINE_COMMENT; // $NON-NLS-1$
	public static final String XQUERY_KEYWORD = DLTKColorConstants.DLTK_KEYWORD; // $NON-NLS-1$
	public static final String XQUERY_DEFAULT = DLTKColorConstants.DLTK_DEFAULT; // $NON-NLS-1$
	public static final String XQUERY_KEYWORD_RETURN = DLTKColorConstants.DLTK_KEYWORD_RETURN; // $NON-NLS-1$
	public static final String XQUERY_FUNCTION_DEFINITION = DLTKColorConstants.DLTK_FUNCTION_DEFINITION;
	public static final String XQUERY_ARGUMENT = "DLTK_argument_definition"; //$NON-NLS-1$
	public static final String XQUERY_NUMBER = DLTKColorConstants.DLTK_NUMBER;
}
