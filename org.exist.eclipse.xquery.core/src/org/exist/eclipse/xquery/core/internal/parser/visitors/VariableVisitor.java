/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import java.lang.reflect.Modifier;

import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.MethodCallExpression;
import org.eclipse.dltk.ast.references.VariableReference;
import org.exist.eclipse.xquery.core.XQueryVariableReference;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.exist.eclipse.xquery.core.internal.parser.ast.XQueryFieldDeclaration;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.Token;
import org.w3c.xqparser.XPathVisitor;

/**
 * Create {@link FieldDeclaration} for global variables in the xquery document.
 * 
 * @author Pascal Schmidiger
 */
public class VariableVisitor implements XPathVisitor, NodeTypes {

	private final XQueryParser _parser;
	private String _name;
	private int _startPos;
	private int _endPos;

	public VariableVisitor(XQueryParser parser) {
		_parser = parser;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		boolean isFun = FUNCTION_QNAME.equals(node.toString());
		if (_name == null && (QNAME.equals(node.toString()) || isFun)) {
			Token token = node.getToken();

			if (token != null) {
				int[] startEnd = ParserVisitor.getNodeStartEnd(_parser, token);
				_startPos = startEnd[0];
				_endPos = startEnd[1];
			}
			_name = node.getValue();
			if (!isFun) {
				_name = "$" + _name;
			}
		}

		node.childrenAccept(this, data);

		return data;
	}

	/**
	 * @return the declaration for the parsed element.
	 */
	public XQueryFieldDeclaration getField(MethodDeclaration parent, boolean publicc) {
		XQueryFieldDeclaration fieldDeclaration = new XQueryFieldDeclaration(parent, _name, _startPos, _endPos);
		fieldDeclaration.setModifiers((parent != null) ? Modifier.PRIVATE : Modifier.PUBLIC);
		return fieldDeclaration;
	}

	/**
	 * @return the declaration for the parsed element.
	 */
	public VariableReference getFieldReference(MethodDeclaration parent) {
		XQueryVariableReference ref = new XQueryVariableReference(parent, _startPos, _endPos, _name);
		return ref;
	}

	public MethodCallExpression getFunctionCall() {
		CallArgumentsList args = new CallArgumentsList();
		MethodCallExpression call = new MethodCallExpression(_startPos, _endPos, null, _name, args);
		return call;
	}
}
