package org.exist.eclipse.xquery.ui.search;

import java.lang.reflect.Field;

import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.internal.core.search.matching.MatchingNodeSet;
import org.eclipse.dltk.internal.core.search.matching.MethodLocator;
import org.eclipse.dltk.internal.core.search.matching.MethodPattern;

@SuppressWarnings("restriction")
public class MyMethodLocator extends MethodLocator {
	public MyMethodLocator(SearchPattern pattern) {
		super((MethodPattern) pattern);
	}

	public int match(CallExpression node, MatchingNodeSet nodeSet) {
		if (!this.pattern.findReferences) {
			return IMPOSSIBLE_MATCH;
		}

		if (this.pattern.selector == null) {
			return nodeSet.addMatch(node, POSSIBLE_MATCH);
		}

		if (matchesName(this.pattern.selector, node.getName().toCharArray())) {
			int nArgs = getFocusMethodArgsCount();
			int nodeArgsSize = node.getArgs().getChilds().size();
			boolean argsOk = nArgs == -1 || nArgs == nodeArgsSize;
			if (argsOk) {
				return nodeSet.addMatch(node, ACCURATE_MATCH);
			}
		}

		return IMPOSSIBLE_MATCH;
	}

	private int getFocusMethodArgsCount() {
		int nArgs = -1;
		try {
			Field declArgsField = MethodPattern.class
					.getDeclaredField("methodArguments");
			declArgsField.setAccessible(true);
			char[][] arr = (char[][]) declArgsField.get(this.pattern);
			nArgs = (arr == null) ? 0 : arr.length;
		} catch (Exception e) {
			e.printStackTrace();
			// ignore, accept all
		}
		return nArgs;
	}
}