package org.exist.eclipse.xquery.core.internal.parser.visitors;

/**
 * All handled node types.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public interface NodeTypes {
	String FUNCTION_CALL = "FunctionCall";
	String FUNCTION_DECL = "FunctionDecl";
	String FUNCTION_QNAME = "FunctionQName";
	String LET_CLAUSE = "LetClause";
	String L_BRACE_EXPR_ENCLOSURE = "LbraceExprEnclosure";
	String PARAM = "Param";
	String PARAM_LIST = "ParamList";
	String QNAME = "QName";
	String R_BRACE = "Rbrace";
	String VAR_DECL = "VarDecl";
	String VAR_NAME = "VarName";
}
