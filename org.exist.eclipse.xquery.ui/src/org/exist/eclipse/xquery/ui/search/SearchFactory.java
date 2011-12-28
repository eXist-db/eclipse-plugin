package org.exist.eclipse.xquery.ui.search;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.MethodCallExpression;
import org.eclipse.dltk.ast.references.Reference;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.search.AbstractSearchFactory;
import org.eclipse.dltk.core.search.IMatchLocatorParser;
import org.eclipse.dltk.core.search.matching.MatchLocator;
import org.eclipse.dltk.core.search.matching.MatchLocatorParser;
import org.eclipse.dltk.core.search.matching.PatternLocator;
import org.exist.eclipse.xquery.core.XQueryVariableReference;

public class SearchFactory extends AbstractSearchFactory {

	public class XQueryMatchLocationParser extends MatchLocatorParser {
		protected XQueryMatchLocationParser(MatchLocator locator) {
			super(locator);
		}

		protected void processStatement(ASTNode node, PatternLocator locator) {
			if (node instanceof MethodCallExpression) {
				locator.match((MethodCallExpression) node, getNodeSet());
			} else if (node instanceof XQueryVariableReference) {
				locator.match((Reference) node, getNodeSet());
			} else {
				locator.match(node, getNodeSet());
			}
		}
	}

	@Override
	public IMatchLocatorParser createMatchParser(MatchLocator locator) {
		return new XQueryMatchLocationParser(locator);
	}

	public String getNormalizedTypeName(IType type) {
		return type.getElementName();
	}
}
