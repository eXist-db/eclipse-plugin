/*
Copyright (c) 2005 W3C(r) (http://www.w3.org/) (MIT (http://www.lcs.mit.edu/),
INRIA (http://www.inria.fr/), Keio (http://www.keio.ac.jp/)),
All Rights Reserved.
See http://www.w3.org/Consortium/Legal/ipr-notice-20000612#Copyright.
W3C liability
(http://www.w3.org/Consortium/Legal/ipr-notice-20000612#Legal_Disclaimer),
trademark
(http://www.w3.org/Consortium/Legal/ipr-notice-20000612#W3C_Trademarks),
document use
(http://www.w3.org/Consortium/Legal/copyright-documents-19990405),
and software licensing rules
(http://www.w3.org/Consortium/Legal/copyright-software-19980720)
apply.
 */
package org.w3c.xqparser;

// ONLY EDIT THIS FILE IN THE GRAMMAR ROOT DIRECTORY!
// THE ONE IN THE ${spec}-src DIRECTORY IS A COPY!!!
public class SimpleNode implements Node {
	protected Node parent;

	protected Node[] children;

	protected int id;

	protected XPath parser;

	public SimpleNode(int i) {
		id = i;
	}

	public SimpleNode(XPath p, int i) {
		this(i);
		parser = p;
	}

	// Factory method
	public static Node jjtCreate(XPath p, int id) {
		return new SimpleNode(p, id);
	}

	@Override
	public void jjtOpen() {
	}

	@Override
	public void jjtClose() {
	}

	@Override
	public void jjtSetParent(Node n) {
		parent = n;
	}

	public void jjtSetChildren(Node[] n) {
		children = n;
	}

	@Override
	public Node jjtGetParent() {
		return parent;
	}

	@Override
	public void jjtAddChild(Node n, int i) {
		if (id == XPathTreeConstants.JJTNCNAME && ((SimpleNode) n).id == XPathTreeConstants.JJTQNAME) {
			m_value = ((SimpleNode) n).m_value;
			_token = ((SimpleNode) n)._token;
			if (m_value.indexOf(':') >= 0)
				throw new PostParseException("Parse Error: NCName can not contain ':'!");
			return;
		}
		// Don't expose the functionQName as a child of a QName!
		else if (id == XPathTreeConstants.JJTQNAME && ((SimpleNode) n).id == XPathTreeConstants.JJTFUNCTIONQNAME) {
			m_value = ((SimpleNode) n).m_value;
			_token = ((SimpleNode) n)._token;
			return;
		}
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	@Override
	public Node jjtGetChild(int i) {
		return children[i];
	}

	@Override
	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(XPathVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	/** Accept the visitor. * */
	public Object childrenAccept(XPathVisitor visitor, Object data) {
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				children[i].jjtAccept(visitor, data);
			}
		}
		return data;
	}

	/*
	 * You can override these two methods in subclasses of SimpleNode to customize
	 * the way the node appears when the tree is dumped. If your output uses more
	 * than one line you should override toString(String), otherwise overriding
	 * toString() is probably all you need to do.
	 */

	@Override
	public String toString() {
		return XPathTreeConstants.jjtNodeName[id];
	}

	public String toString(String prefix) {
		return prefix + toString();
	}

	/*
	 * Override this method if you want to customize how the node dumps out its
	 * children.
	 */

	public void dump(String prefix) {
		dump(prefix, System.out);
	}

	public void dump(String prefix, java.io.PrintStream ps) {
		ps.print(toString(prefix));
		printValue(ps);
		ps.println();
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode) children[i];
				if (n != null) {
					n.dump(prefix + "   ", ps);
				}
			}
		}
	}

	// Manually inserted code begins here

	protected String m_value;
	protected Token _token;

	public void processToken(Token t) {
		setValue(t.image);
		if (parser.token != null) {
			_token = new Token();
			_token.beginColumn = parser.token.beginColumn;
			_token.beginLine = parser.token.beginLine;
			_token.endColumn = parser.token.endColumn;
			_token.endLine = parser.token.endLine;
			_token.image = parser.token.image;
			_token.kind = parser.token.kind;
		}
	}

	public void processValue(String val) {
		setValue(val);
	}

	public void printValue(java.io.PrintStream ps) {
		if (null != m_value)
			ps.print(" " + m_value);
	}

	public String getValue() {
		return m_value;
	}

	public void setValue(String m_value) {
		this.m_value = m_value;
	}

	private Object _userValue;

	protected Object getUserValue() {
		return _userValue;
	}

	protected void setUserValue(Object userValue) {
		_userValue = userValue;
	}

	/**
	 * @return the token
	 */
	public final Token getToken() {
		return _token;
	}

}
