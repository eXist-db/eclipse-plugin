package org.exist.eclipse.xquery.ui.internal.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner for partitioning the content in different partitions.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryPartitionScanner extends RuleBasedPartitionScanner {
	public XQueryPartitionScanner() {
		IToken string = new Token(IXQueryPartitions.XQUERY_STRING);
		IToken comment = new Token(IXQueryPartitions.XQUERY_COMMENT);

		List<IRule> rules = new ArrayList<>();

		rules.add(new MultiLineRule("(:", ":)", comment));

		rules.add(new MultiLineRule("\"\"\"", "\"\"\"", string, '\\'));
		rules.add(new MultiLineRule("\'\'\'", "\'\'\'", string, '\\'));
		rules.add(new MultiLineRule("\'", "\'", string, '\\'));
		rules.add(new MultiLineRule("\"", "\"", string, '\\'));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}