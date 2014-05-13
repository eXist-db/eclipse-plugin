package org.exist.eclipse.xquery.core.internal.parser;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;

/**
 * Returns instances of the XQuery source parser. This class is registered on an
 * extension.
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySourceParserFactory implements ISourceParserFactory {

	@Override
	public ISourceParser createSourceParser() {
		return new XQuerySourceParser();
	}
}
