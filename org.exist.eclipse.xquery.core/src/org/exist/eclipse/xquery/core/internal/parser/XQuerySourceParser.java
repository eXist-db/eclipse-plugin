package org.exist.eclipse.xquery.core.internal.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.IModuleDeclaration;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

/**
 * Entry point for dltk to parse the xquery document and create the "AST Node"
 * model.
 * 
 * @see XQuerySourceParserFactory
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySourceParser extends AbstractSourceParser {

	public XQuerySourceParser() {
	}

	public IModuleDeclaration parse(IModuleSource input,
			IProblemReporter reporter) {
		ModuleDeclaration moduleDeclaration = new ModuleDeclaration(input
				.getSourceContents().length(), true);
		XQueryParser parser = new XQueryParser(moduleDeclaration, input
				.getFileName().toCharArray(), input.getContentsAsCharArray(),
				reporter);
		parser.parse();
		moduleDeclaration.rebuild();
		return moduleDeclaration;
	}
}
