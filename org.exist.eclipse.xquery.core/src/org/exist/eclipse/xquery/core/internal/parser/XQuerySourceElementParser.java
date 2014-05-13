package org.exist.eclipse.xquery.core.internal.parser;

import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.exist.eclipse.xquery.core.XQueryNature;
import org.exist.eclipse.xquery.core.internal.parser.visitors.XQuerySourceElementRequestVisitor;

/**
 * Entry point for dltk to create the "Element" model. This class is registered
 * on an extension.
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySourceElementParser extends AbstractSourceElementParser {

	@Override
	protected String getNatureId() {
		return XQueryNature.NATURE_ID;
	}

	@Override
	protected SourceElementRequestVisitor createVisitor() {
		return new XQuerySourceElementRequestVisitor(getRequestor());
	}
}
