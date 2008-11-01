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
	private static final String ATOMIC_TYPE = "AtomicType";
	private static final String OCCURENCE_INDICATOR = "OccurrenceIndicator";
	private static final String DEFAULT = "element";
	private XQueryParser _parser;
	private String _name;
	private int _startPos;
	private int _endPos;
	private boolean _hasAtomicType;

	public ParameterVisitor(XQueryParser parser) {
		_parser = parser;
		_name = DEFAULT;
	}

	public Object visit(SimpleNode node, Object data) {
		if (ATOMIC_TYPE.equals(node.toString())) {
			_hasAtomicType = true;
			node.childrenAccept(this, data);
		} else if (QNAME.equals(node.toString())) {
			if (_hasAtomicType) {
				_name = node.getValue();
				_endPos += node.getToken().endColumn;
			} else {
				if (node.getToken() != null) {
					_startPos = _parser.getStartPosition(0,
							node.getToken().beginLine);
					_endPos = _parser.getStartPosition(_startPos, node
							.getToken().endLine
							- node.getToken().beginLine);
					_startPos += (node.getToken().beginColumn - 1);
					_endPos += (node.getToken().endColumn);
				}
			}
		} else if (OCCURENCE_INDICATOR.equals(node.toString())) {
			_name += node.getValue();
			_endPos++;
		} else {
			node.childrenAccept(this, data);
		}

		return data;
	}

	/**
	 * @return the created {@link Argument}
	 */
	public Argument getArgument() {
		return new Argument(new SimpleReference(_startPos, _endPos, _name
				.toString()), _startPos, null, 0);
	}

}
