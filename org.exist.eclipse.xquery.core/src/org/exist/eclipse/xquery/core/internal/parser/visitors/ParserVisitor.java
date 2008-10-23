/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPathVisitor;

/**
 * Start point for traversing the whole {@link SimpleNode} from parser and
 * distribute the itemization node to specific visitors.
 * 
 * @see FunctionVisitor
 * @see VariableVisitor
 * 
 * @author Pascal Schmidiger
 */
public class ParserVisitor implements XPathVisitor {
	private static final String FUNCTION_DECL = "FunctionDecl";
	private static final String VAR_DECL = "VarDecl";

	private final XQueryParser _parser;
	private final ModuleDeclaration _moduleDeclaration;

	public ParserVisitor(XQueryParser parser,
			ModuleDeclaration moduleDeclaration) {
		_parser = parser;
		_moduleDeclaration = moduleDeclaration;
	}

	public Object visit(SimpleNode node, Object data) {
		if (FUNCTION_DECL.equals(node.toString())) {
			FunctionVisitor visitor = new FunctionVisitor(_parser);
			Object accept = node.childrenAccept(visitor, data);
			_moduleDeclaration.addStatement(visitor.getMethod());
			return accept;
		} else if (VAR_DECL.equals(node.toString())) {
			VariableVisitor visitor = new VariableVisitor(_parser);
			Object accept = node.childrenAccept(visitor, data);
			_moduleDeclaration.addStatement(visitor.getField());
			return accept;
		}

		return node.childrenAccept(this, data);
	}
}
