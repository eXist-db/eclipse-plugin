/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser;

import java.io.StringReader;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.exist.eclipse.xquery.core.internal.parser.visitors.ParserVisitor;
import org.w3c.xqparser.ParseException;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPath;

/**
 * This class parsed the xquery with a parser from <a
 * href="http://www.w3.org/">W3.org</a> and checked the syntax. The parser could
 * only highlight one syntax error. The parser has two problems which this class
 * have to solve:
 * <ul>
 * <li>Could not parse comments, therefore the class have to remove comments
 * from xquery before parsing it</li>
 * <li>Give back the line and the position in line, where the syntax error is:
 * The problem report for eclipse needs the absolute position in the whole
 * content, therefore the position must be evaluate</li>
 * </ul>
 * 
 * @author Pascal Schmidiger
 */
public class XQueryParser {

	private final char[] _content;
	private final IProblemReporter _problemReporter;
	private final char[] _fileName;
	private final ModuleDeclaration _moduleDeclaration;

	public XQueryParser(ModuleDeclaration moduleDeclaration, char[] fileName,
			char[] content, IProblemReporter problemReporter) {
		_moduleDeclaration = moduleDeclaration;
		_fileName = fileName;
		_content = content;
		_problemReporter = problemReporter;
	}

	/**
	 * Parsing the document.
	 */
	public void parse() {
		removeComment(_content);
		XPath parser = new XPath(new StringReader(new String(_content)));
		try {
			SimpleNode node = parser.XPath2();
			ParserVisitor visitor = new ParserVisitor(this, _moduleDeclaration);
			node.childrenAccept(visitor, null);
		} catch (ParseException e) {
			reportError(e);
		}
	}

	/**
	 * Get the absolut startposition in the document at the beginning of the
	 * given <code>lineCount</code>. With the given <code>start</code>
	 * parameter, you could start between the document.
	 * 
	 * @param start
	 *            Absolut position in document, where you want to start.
	 * @param lineCount
	 *            Count of lines, where you want to add the start.
	 * @return Absolute position in the document: start + signs for lineCount.
	 */
	public int getStartPosition(int start, int lineCount) {
		int startPosOnLine = start;
		for (int i = 1; i < lineCount; i++) {
			boolean eof = false;
			while (!eof && startPosOnLine < _content.length) {
				int sign = (int) _content[startPosOnLine++];

				if (sign == 13) {
					if ((startPosOnLine) < _content.length) {
						int nextsign = (int) _content[startPosOnLine];
						if (nextsign == 10) {
							startPosOnLine++;
						}
					}
					eof = true;
				} else if (sign == 10) {
					if ((startPosOnLine) < _content.length) {
						int nextsign = (int) _content[startPosOnLine];
						if (nextsign == 13) {
							startPosOnLine++;
						}
					}
					eof = true;
				}
			}
		}
		return startPosOnLine;
	}

	public final char[] getContent() {
		return _content;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////

	private void reportError(ParseException e) {
		try {
			String[] messages = { "Syntax Error: " + e.getMessage() };

			int startPos = getStartPosition(0, e.currentToken.next.beginLine);
			int endPos = getStartPosition(startPos, e.currentToken.next.endLine
					- e.currentToken.next.beginLine);
			startPos += (e.currentToken.next.beginColumn - 1);
			endPos += (e.currentToken.next.endColumn);

			if (endPos > 0 && endPos > (_content.length - 1)) {
				endPos = _content.length - 1;
			}
			if (startPos < 0) {
				startPos = 0;
			}

			DefaultProblem defaultProblem = new DefaultProblem(new String(
					_fileName), messages[0], IProblem.Syntax, new String[] {},
					ProblemSeverities.Error, startPos, endPos,
					e.currentToken.next.beginLine - 1);

			_problemReporter.reportProblem(defaultProblem);
		} catch (Exception ignore) {

		}
	}

	private void removeComment(char[] content) {
		for (int i = 0; i < content.length - 1; i++) {
			if (detectStartSequence(content[i], content[i + 1])) {
				// replace with empty sign
				char last = ' ';

				boolean replace = true;
				while (replace && i < content.length) {
					if (detectEndSequence(last, content[i])) {
						if (couldReplace(content[i])) {
							content[i] = ' ';
						}
						replace = false;
					} else {
						last = content[i];
						if (couldReplace(content[i])) {
							content[i] = ' ';
						}
						i++;
					}
				}
			}
		}
	}

	private boolean detectStartSequence(char content0, char content1) {
		if (content0 == '(' && content1 == ':') {
			return true;
		}
		return false;
	}

	private boolean detectEndSequence(char content0, char content1) {
		if (content0 == ':' && content1 == ')') {
			return true;
		}
		return false;
	}

	private boolean couldReplace(int sign) {
		if (sign == 10 || sign == 13) {
			return false;
		}
		return true;
	}
}
