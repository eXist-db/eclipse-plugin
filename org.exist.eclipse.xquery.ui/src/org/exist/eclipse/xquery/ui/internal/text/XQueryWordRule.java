package org.exist.eclipse.xquery.ui.internal.text;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * This class is a copy of the WordRule, with the exception that when we
 * detected a 'function', the next default token is not the regular, but the
 * token identifying the function name.
 * 
 * The changes were:
 * 
 * added the funcNameToken attributes (and constructor parameters) changed the
 * evaluate to store the last found and return things accordingly
 * 
 * @see IWordDetector
 */
public class XQueryWordRule implements IRule {

	/** Internal setting for the un-initialized column constraint */
	protected static final int UNDEFINED = -1;

	/** The word detector used by this rule */
	protected IWordDetector _detector;
	/**
	 * The default token to be returned on success and if nothing else has been
	 * specified.
	 */
	protected IToken _defaultToken;
	/** The column constraint */
	protected int _column;
	/** The table of predefined words and token for this rule */
	protected Map<String, IToken> _words;
	/**
	 * Buffer used for pattern detection
	 */
	private StringBuilder _buffer;

	private final IToken _argumentToken;

	private final IToken _funcToken;

	private String _lastFound;

	/**
	 * Creates a rule which, with the help of a word detector, will return the token
	 * associated with the detected word. If no token has been associated, the
	 * specified default token will be returned.
	 * 
	 * @param detector      the word detector to be used by this rule, may not be
	 *                      <code>null</code>
	 * @param defaultToken  the default token to be returned on success if nothing
	 *                      else is specified, may not be <code>null</code>
	 * @param funcToken     token for function name
	 * @param argumentToken token for arguments
	 * 
	 * @see #addWord(String, IToken)
	 */
	public XQueryWordRule(IWordDetector detector, IToken defaultToken, IToken funcToken, IToken argumentToken) {
		_lastFound = "";
		_buffer = new StringBuilder();
		_words = new HashMap<>();
		_column = UNDEFINED;
		Assert.isNotNull(detector);
		Assert.isNotNull(defaultToken);

		_detector = detector;
		_defaultToken = defaultToken;
		_funcToken = funcToken;
		_argumentToken = argumentToken;
	}

	/**
	 * Adds a word and the token to be returned if it is detected.
	 * 
	 * @param word  the word this rule will search for, may not be <code>null</code>
	 * @param token the token to be returned if the word has been found, may not be
	 *              <code>null</code>
	 */
	public void addWord(String word, IToken token) {
		Assert.isNotNull(word);
		Assert.isNotNull(token);

		_words.put(word, token);
	}

	/**
	 * Sets a column constraint for this rule. If set, the rule's token will only be
	 * returned if the pattern is detected starting at the specified column. If the
	 * column is smaller then 0, the column constraint is considered removed.
	 * 
	 * @param column the column in which the pattern starts
	 */
	public void setColumnConstraint(int column) {
		if (column < 0) {
			column = UNDEFINED;
		}
		_column = column;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (_detector.isWordStart((char) c)) {
			if (_column == UNDEFINED || (_column == scanner.getColumn() - 1)) {

				_buffer.setLength(0);
				do {
					_buffer.append((char) c);
					c = scanner.read();
				} while (c != ICharacterScanner.EOF && _detector.isWordPart((char) c));
				scanner.unread();

				String str = _buffer.toString();
				IToken token = _words.get(str);
				if (token != null) {
					_lastFound = str;
					return token;
				}

				if (_defaultToken.isUndefined()) {
					unreadBuffer(scanner);
				}
				if (str.startsWith("$")) {
					_lastFound = str;
					return _argumentToken;
				}

				if ((_lastFound.equals("function"))) {
					_lastFound = str;
					return _funcToken;
				}
				return _defaultToken;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	/**
	 * Returns the characters in the buffer to the scanner.
	 * 
	 * @param scanner the scanner to be used
	 */
	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i = _buffer.length() - 1; i >= 0; i--) {
			scanner.unread();
		}
	}
}
