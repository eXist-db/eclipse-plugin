package org.exist.eclipse.xquery.ui.internal.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Scanner which highlighted the code in a xquery.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCodeScanner extends AbstractScriptScanner {
	private static String _returnKeyword = "return";

	private static String _tokenProperties[] = new String[] {
			IXQueryColorConstants.XQUERY_DEFAULT,
			IXQueryColorConstants.XQUERY_KEYWORD,
			IXQueryColorConstants.XQUERY_KEYWORD_RETURN,
			IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION,
			IXQueryColorConstants.XQUERY_ARGUMENT };

	public XQueryCodeScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		initialize();
	}

	@Override
	protected String[] getTokenProperties() {
		return _tokenProperties;
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken keyword = this.getToken(IXQueryColorConstants.XQUERY_KEYWORD);
		IToken keywordReturn = getToken(IXQueryColorConstants.XQUERY_KEYWORD_RETURN);
		IToken other = this.getToken(IXQueryColorConstants.XQUERY_DEFAULT);
		IToken func = this
				.getToken(IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION);
		IToken argument = this.getToken(IXQueryColorConstants.XQUERY_ARGUMENT);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new XQueryWhitespaceDetector()));

		// Add word rule for keywords.
		XQueryWordRule wordRule = new XQueryWordRule(new XQueryWordDetector(),
				other, func, argument);
		KeyWordContainer wordContainer = XQueryUI.getDefault()
				.getKeyWordContainer();
		for (String word : wordContainer.getKeyWords()) {
			wordRule.addWord(word, keyword);
		}
		wordRule.addWord(_returnKeyword, keywordReturn);
		rules.add(wordRule);

		this.setDefaultReturnToken(other);
		return rules;
	}
}
