/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPathVisitor;

/**
 * Start point for traversing the parameter {@link SimpleNode} from parser and
 * create a new {@link Argument}.
 * 
 * @author Pascal Schmidiger
 */
public class ParameterVisitor implements XPathVisitor {
	private static final String QNAME = "QName";
	private XQueryParser _parser;
	private String _name;
	private int _startPos;
	private int _endPos;

	public ParameterVisitor(XQueryParser parser) {
		_parser = parser;
		_name = null;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		if (QNAME.equals(node.toString())) {
			_name = "$" + node.getValue();
			if (node.getToken() != null) {
				int[] startEnd = ParserVisitor.getNodeStartEnd(_parser, node
						.getToken());
				_startPos = startEnd[0];
				_endPos = startEnd[1];
			}
		}

		return data;
	}

	/**
	 * @return the created {@link Argument}
	 */
	public Argument getArgument() {
		Argument arg = new Argument(new SimpleReference(_startPos, _endPos,
				_name), _startPos, null, 0);
		arg.getRef().setStart(_startPos);
		arg.getRef().setEnd(_startPos + _name.length() - 1);
		return arg;
	}

}
