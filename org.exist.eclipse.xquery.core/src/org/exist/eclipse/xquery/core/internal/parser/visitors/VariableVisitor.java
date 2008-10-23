/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import java.lang.reflect.Modifier;

import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.exist.eclipse.xquery.core.internal.parser.ast.XQueryFieldDeclaration;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPathVisitor;

/**
 * Create {@link FieldDeclaration} for global variables in the xquery document.
 * 
 * @author Pascal Schmidiger
 */
public class VariableVisitor implements XPathVisitor {
	private static final String QNAME = "QName";

	private final XQueryParser _parser;
	private String _name;
	private int _startPos;
	private int _endPos;
	private String _value;

	public VariableVisitor(XQueryParser parser) {
		_parser = parser;
	}

	public Object visit(SimpleNode node, Object data) {
		if (QNAME.equals(node.toString())) {
			_startPos = _parser.getStartPosition(0, node.getToken().beginLine);
			_endPos = _parser.getStartPosition(_startPos,
					node.getToken().endLine - node.getToken().beginLine);
			_startPos += (node.getToken().beginColumn - 1);
			_endPos += (node.getToken().endColumn);
			_name = "$" + node.getValue();
		} else {
			_value = node.getValue();
		}
		return data;
	}

	/**
	 * @return the declaration for the parsed element.
	 */
	public FieldDeclaration getField() {
		XQueryFieldDeclaration fieldDeclaration = new XQueryFieldDeclaration(
				_name, _value, _startPos, _endPos, _startPos, _endPos);
		fieldDeclaration.setModifier(Modifier.PRIVATE);
		return fieldDeclaration;
	}
}
