package org.exist.eclipse.xquery.ui.internal.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

/**
 * Scanner which highlighted the comment in a xquery.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCommentScanner extends AbstractScriptScanner {
	private static String _tokenProperties[] = new String[] { IXQueryColorConstants.XQUERY_COMMENT };

	public XQueryCommentScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		initialize();
	}

	protected String[] getTokenProperties() {
		return _tokenProperties;
	}

	protected List<IRule> createRules() {
		IToken comment = getToken(IXQueryColorConstants.XQUERY_COMMENT);
		setDefaultReturnToken(comment);
		List<IRule> rules = new ArrayList<IRule>();
		return rules;
	}
}
