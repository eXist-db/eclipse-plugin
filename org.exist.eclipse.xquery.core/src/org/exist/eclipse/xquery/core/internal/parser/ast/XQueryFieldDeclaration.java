package org.exist.eclipse.xquery.core.internal.parser.ast;

import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Represents a global variable in a xquery document.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryFieldDeclaration extends FieldDeclaration {

	private final String _initialization;

	public XQueryFieldDeclaration(String name, String initialization,
			int nameStart, int nameEnd, int declStart, int declEnd) {
		super(name, nameStart, nameEnd, declStart, declEnd);
		_initialization = initialization;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getName());
		if (_initialization != null && _initialization.length() > 0) {
			sb.append('=');
			sb.append(_initialization);
		}
		return sb.toString();
	}

	@Override
	public void printNode(CorePrinter output) {
		output.formatPrint("Field" + this.getSourceRange().toString() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		output.formatPrintLn(super.toString());
	}

}
