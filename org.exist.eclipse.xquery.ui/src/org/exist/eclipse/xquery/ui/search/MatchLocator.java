package org.exist.eclipse.xquery.ui.search;

import java.lang.reflect.Modifier;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.indexing.IIndexConstants;
import org.eclipse.dltk.core.search.matching.PossibleMatch;
import org.eclipse.dltk.core.search.matching.PossibleMatchSet;

public class MatchLocator extends org.eclipse.dltk.core.search.matching.MatchLocator {
	@Override
	public void initialize(SearchPattern searchPattern, IDLTKSearchScope searchScope) {
		super.initialize(searchPattern, searchScope);
		if (searchPattern.kind == IIndexConstants.FIELD_PATTERN) {
			patternLocator = new MyFieldLocator(searchPattern);
		} else if (searchPattern.kind == IIndexConstants.METHOD_PATTERN) {
			patternLocator = new MyMethodLocator(searchPattern);
		}
		matchContainer = patternLocator.matchContainer();
	}

	@Override
	protected void locateMatches(IScriptProject scriptProject, PossibleMatchSet matchSet, int expected)
			throws CoreException {

		IModelElement focus = this.pattern.focus;

		PossibleMatchSet newMatchSet = null;

		if (focus instanceof IField) {
			boolean isPrivate = (((IField) focus).getFlags() & Modifier.PRIVATE) > 0;
			if (isPrivate) {
				newMatchSet = restrictToFocusResource(matchSet, focus);
			}
		}

		if (newMatchSet == null) {
			newMatchSet = matchSet;
		}

		super.locateMatches(scriptProject, newMatchSet, expected);
	}

	private PossibleMatchSet restrictToFocusResource(PossibleMatchSet matchSet, IModelElement focus) {
		IModelElement sourceModule = focus.getAncestor(IModelElement.SOURCE_MODULE);

		IProjectFragment pf = (IProjectFragment) sourceModule.getAncestor(IModelElement.PROJECT_FRAGMENT);

		PossibleMatch[] all = matchSet.getPossibleMatches(new IProjectFragment[] { pf });

		PossibleMatchSet result = new PossibleMatchSet();
		String thisFileName = sourceModule.getResource().getName();
		for (PossibleMatch it : all) {
			if (it.getFileName().equals(thisFileName)) {
				result.add(it);
				break;
			}
		}
		return result;
	}
}
