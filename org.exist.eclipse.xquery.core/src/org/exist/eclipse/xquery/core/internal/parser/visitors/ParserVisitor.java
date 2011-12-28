/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import java.lang.reflect.Modifier;

import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.MethodCallExpression;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.exist.eclipse.xquery.core.XQueryVariableReference;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.exist.eclipse.xquery.core.internal.parser.ast.XQueryFieldDeclaration;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.Token;
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
public class ParserVisitor implements XPathVisitor, NodeTypes {

	private final XQueryParser _parser;
	private final ModuleDeclaration _moduleDeclaration;
	private boolean _skipNextVarName;
	private MethodDeclaration _lastMethod;

	public ParserVisitor(XQueryParser parser,
			ModuleDeclaration moduleDeclaration) {
		_parser = parser;
		_moduleDeclaration = moduleDeclaration;
	}

	/**
	 * @return nullable
	 */
	public static Token findFirstToken(SimpleNode node) {
		final Token[] t = { node.getToken() };
		if (t[0] == null) {
			node.childrenAccept(new XPathVisitor() {
				public Object visit(SimpleNode node, Object data) {
					if (t[0] == null) {
						t[0] = node.getToken();
						if (t[0] == null) {
							node.childrenAccept(this, null);
						}
					}
					return null;
				}
			}, null);
		}
		return t[0];
	}

	@SuppressWarnings("unchecked")
	public Object visit(SimpleNode node, Object data) {

		if (_lastMethod != null) {
			Token token = findFirstToken(node);
			if (token != null) {
				int mEnd = _lastMethod.sourceEnd();

				int[] nodeStartEnd = getNodeStartEnd(_parser, token);

				if (nodeStartEnd[0] > mEnd) {
					_lastMethod = null;
				}
			}
		}

		if (_skipNextVarName
				&& ("VarName".equals(node.toString()) || "QName".equals(node
						.toString()))) {
			if (node.jjtGetNumChildren() == 0) {
				_skipNextVarName = false;
			} else if (!"VarName".equals(node.jjtGetChild(0).toString())
					&& !"QName".equals(node.jjtGetChild(0).toString())) {
				_skipNextVarName = false;
			}
			return node.childrenAccept(this, data);
		}

		if (FUNCTION_DECL.equals(node.toString())) {
			FunctionVisitor visitor = new FunctionVisitor(_parser);
			node.childrenAccept(visitor, data);
			_lastMethod = visitor.getMethod();

			for (Object a : _lastMethod.getArguments()) {
				Argument arg = (Argument) a;
				XQueryFieldDeclaration fieldDeclaration = new XQueryFieldDeclaration(
						_lastMethod, arg.getName(), arg.getNameStart(), arg
								.getNameEnd());
				fieldDeclaration.setModifiers(Modifier.PRIVATE);
				_lastMethod.getStatements().add(fieldDeclaration);
			}

			_moduleDeclaration.addStatement(_lastMethod);
			// keep on visiting let clauses in function
		} else if (LET_CLAUSE.equals(node.toString())) {
			VariableVisitor visitor = new VariableVisitor(_parser);
			node.jjtGetChild(0).jjtGetChild(0).jjtAccept(visitor, data);
			XQueryFieldDeclaration field = visitor.getField(_lastMethod, false);
			addStatementToLastMethodOrModule(field);
			_skipNextVarName = true;
		} else if (VAR_DECL.equals(node.toString())) {
			VariableVisitor visitor = new VariableVisitor(_parser);
			node.childrenAccept(visitor, data);
			_moduleDeclaration
					.addStatement(visitor.getField(_lastMethod, true));
			_skipNextVarName = true;
		} else if (VAR_NAME.equals(node.toString())) {
			VariableVisitor visitor = new VariableVisitor(_parser);
			node.childrenAccept(visitor, data);
			VariableReference aa = visitor.getFieldReference(_lastMethod);
			addStatementToLastMethodOrModule(aa);
		} else if (FUNCTION_CALL.equals(node.toString())) {
			VariableVisitor visitor = new VariableVisitor(_parser);
			node.jjtAccept(visitor, data);
			MethodCallExpression methodCall = visitor.getFunctionCall();
			for (int i = 0, n = node.jjtGetNumChildren() - 1; i < n; i++) {
				methodCall.getArgs().addNode(
						new CallExpression(null, "<arg" + i + ">",
								new CallArgumentsList()));
			}
			addStatementToLastMethodOrModule(methodCall);
		} else if (QNAME.equals(node.toString())) {
			// don't add refs for params
			if (!PARAM.equals(node.jjtGetParent().toString())) {
				Token token = findFirstToken(node);
				if (token != null) {
					int[] nodeStartEnd = getNodeStartEnd(_parser, token);
					addStatementToLastMethodOrModule(new XQueryVariableReference(
							_lastMethod, nodeStartEnd[0], nodeStartEnd[1], "$"
									+ token.toString()));
				}
			}
		}

		return node.childrenAccept(this, data);
	}

	@SuppressWarnings("unchecked")
	private void addStatementToLastMethodOrModule(Statement field) {
		if (_lastMethod != null) {
			_lastMethod.getStatements().add(field);
		} else {
			_moduleDeclaration.addStatement(field);
		}
	}

	public static int[] getNodeStartEnd(XQueryParser parser, Token token) {
		int beginLine = token.beginLine;
		int endLine = token.endLine;
		int beginColumn = token.beginColumn;
		int endColumn = token.endColumn;
		int startPos = parser.getStartPosition(0, beginLine);
		int endPos = parser.getStartPosition(0, endLine);
		startPos += beginColumn - 1;
		endPos += endColumn;
		return new int[] { startPos, endPos };
	}
}
