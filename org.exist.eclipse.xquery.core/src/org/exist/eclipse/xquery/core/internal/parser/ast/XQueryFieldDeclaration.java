package org.exist.eclipse.xquery.core.internal.parser.ast;

import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Represents a global variable in a xquery document.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryFieldDeclaration extends FieldDeclaration {

	private MethodDeclaration _parent;

	/**
	 * @param parent nullable if in module scope
	 */
	public XQueryFieldDeclaration(MethodDeclaration parent, String name, int nameStart, int nameEnd) {
		super(name, nameStart, nameEnd, nameStart, nameEnd);
	}

	@Override
	public void printNode(CorePrinter output) {
		output.formatPrint("Field" + this.getSourceRange().toString() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		output.formatPrintLn(super.toString());
	}

	public MethodDeclaration getMethodDeclaration() {
		return _parent;
	}

}
