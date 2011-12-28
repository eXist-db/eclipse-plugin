package org.exist.eclipse.xquery.core.internal.parser.visitors;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPathVisitor;

/**
 * Start point for traversing the function {@link SimpleNode} from parser,
 * create a new {@link MethodDeclaration} and distribute the parameters to
 * specific visitor {@link ParameterListVisitor}.
 * 
 * @see ParameterListVisitor
 * 
 * @author Pascal Schmidiger
 */
public class FunctionVisitor implements XPathVisitor, NodeTypes {

	private final XQueryParser _parser;
	private String _name;
	private int _startPos;
	private int _endPos;
	private int _declStart;
	private int _declEnd;
	private boolean _bracketOpen = false;
	private ParameterListVisitor _parameterListVisitor;

	public FunctionVisitor(XQueryParser parser) {
		_parser = parser;
	}

	public Object visit(SimpleNode node, Object data) {
		if (QNAME.equals(node.toString()) && _name == null) {
			int[] startEnd = ParserVisitor.getNodeStartEnd(_parser, node
					.getToken());
			_startPos = startEnd[0];
			_endPos = startEnd[1];
			_name = node.getValue();
		} else if (PARAM_LIST.equals(node.toString())) {
			_parameterListVisitor = new ParameterListVisitor(_parser);
			node.childrenAccept(_parameterListVisitor, data);
		} else if (L_BRACE_EXPR_ENCLOSURE.equals(node.toString())) {
			_declStart = _parser.getStartPosition(0, node.getToken().beginLine);
			_declStart += (node.getToken().beginColumn - 1);
			_bracketOpen = true;
		} else if (R_BRACE.equals(node.toString())) {
			_declEnd = _parser.getStartPosition(0, node.getToken().beginLine);
			_declEnd += (node.getToken().beginColumn - 1);
			_bracketOpen = false;
		} else {
			if (!_bracketOpen) {
				node.childrenAccept(this, data);
			}
		}
		return data;
	}

	/**
	 * @return a {@link MethodDeclaration} contains the specific parameter.
	 */
	public MethodDeclaration getMethod() {
		MethodDeclaration methodDeclaration = new MethodDeclaration(_name,
				_startPos, _endPos, _declStart, _declEnd);

		if (_parameterListVisitor != null
				&& _parameterListVisitor.hasArguments()) {
			methodDeclaration.acceptArguments(_parameterListVisitor
					.getArguments());
		}
		return methodDeclaration;
	}

}
