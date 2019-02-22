package org.exist.eclipse.xquery.core;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.references.VariableReference;

/**
 * Represents a variable reference.
 * 
 * @author Christian Oetterli
 */
public class XQueryVariableReference extends VariableReference {

	private final MethodDeclaration _parent;

	/**
	 * @param parent nullable if in module scope
	 */
	public XQueryVariableReference(MethodDeclaration parent, int nameStart, int nameEnd, String name) {
		super(nameStart, nameEnd, name);
		_parent = parent;
	}

	public MethodDeclaration getParent() {
		return _parent;
	}

}
