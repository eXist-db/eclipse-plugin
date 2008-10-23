package org.exist.eclipse.xquery.core.internal.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
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

	public ModuleDeclaration parse(char[] fileName, char[] content0,
			IProblemReporter reporter) {// throws
		ModuleDeclaration moduleDeclaration = new ModuleDeclaration(
				content0.length, true);
		XQueryParser parser = new XQueryParser(moduleDeclaration, fileName,
				content0, reporter);
		parser.parse();
		moduleDeclaration.rebuild();
		return moduleDeclaration;
	}
}
