package org.exist.eclipse.xquery.ui.internal.selection;

import org.eclipse.dltk.codeassist.ScriptSelectionEngine;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IForeignElement;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.model.LocalVariable;
import org.eclipse.dltk.internal.ui.editor.ExternalStorageEditorInput;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.exist.eclipse.xquery.ui.internal.completion.LibDocument;
import org.exist.eclipse.xquery.ui.internal.completion.XQueryMixinModel;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * @author Christian Oetterli
 * @version $Id: $
 */
public class XQuerySelectionEngine extends ScriptSelectionEngine {

	public static String findWord(String text, int pos) {
		return findWord(text, pos, null);
	}

	public static String findWord(String text, int pos, int[] outRight) {
		if (pos > 0 && pos < text.length() && text.charAt(pos) == ' ') {
			pos--;
		}
		int len = text.length();

		int left = pos;
		while (left > 0 && isWordPart(text.charAt(left - 1))) {
			left--;
		}

		int right = pos;
		while (right < len && isWordPart(text.charAt(right))) {
			right++;
		}

		String result = text.substring(left, right).trim();
		if (!result.isEmpty() && Character.isDigit(result.charAt(0))) {
			result = ""; // must not start with a digit
		}

		if (outRight != null) {
			outRight[0] = right;
		}

		return result;
	}

	protected static boolean isWordPart(char c) {
		return Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == '$'
				|| c == ':';
	}

	public XQuerySelectionEngine() {
	}

	public IModelElement[] select(IModuleSource module, final int caret, int i) {
		try {

			final ISourceModule mod = (ISourceModule) module.getModelElement();

			String source = mod.getSource();
			final int[] right = new int[1];
			final String word = findWord(source, caret, right);

			if (word.isEmpty()) {
				return null;
			}

			// currently we select functions (and their parameters) and fields
			boolean isFunction = word.charAt(0) != '$';
			final int argCount;

			if (isFunction) {
				argCount = parseArgCount(source, right[0]);
				IModelElement fe = openLibraryFunction(mod, word, caret,
						argCount);
				if (fe != null) {
					return new IModelElement[] { fe };
				}

			} else {
				argCount = -1;
			}

			final IModelElement[] result = new IModelElement[1];

			if (isFunction) {
				mod.accept(new IModelElementVisitor() {
					public boolean visit(IModelElement element) {
						try {
							if ((element instanceof IMethod)
									&& element.getElementName().equals(word)) {
								IMethod m = (IMethod) element;
								if (m.getParameters().length == argCount) {
									result[0] = element; // found
								}
							}

							return result[0] == null;
						} catch (ModelException e) {
							throw new RuntimeException(e);
						}
					}
				});
			} else {
				final IMethod[] coveringFun = new IMethod[1];
				final IField[] field = new IField[1];
				mod.accept(new IModelElementVisitor() {
					public boolean visit(IModelElement element) {
						if (element instanceof IField
								&& element.getElementName().equals(word)) {
							field[0] = (IField) element;
						} else if (element instanceof IMethod) {
							if (isCoveringFunction((IMethod) element, caret)) {
								coveringFun[0] = (IMethod) element;
							}
						}

						return true;
					}
				});

				if (coveringFun[0] == null) {
					result[0] = field[0];
				} else {
					for (IModelElement it : coveringFun[0].getChildren()) {
						if (it instanceof IField
								&& it.getElementName().equals(word)) {
							result[0] = it;
							break;
						}
					}
				}
			}

			return result[0] == null ? null : result;
		} catch (ModelException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Used to open a library function. The foreign element is returned from
	 * select (prevent not-found 'ping' sound) but does not change caret
	 * location.
	 */
	private static abstract class ForeignElement extends LocalVariable
			implements IForeignElement {

		public ForeignElement(IModelElement parent, String name, int start) {
			super(parent, name, start, 0, start, 0, "");
		}
	}

	private IModelElement openLibraryFunction(IModelElement parent,
			String fqMethodName, final int caret, final int argCount) {
		final IModelElement[] isLib = new IModelElement[1];
		int prefixPos = fqMethodName.indexOf(':');
		String prefix;
		final String methodName;
		if (prefixPos == -1) {
			prefix = XQueryMixinModel.PREFIX_FN;
			methodName = fqMethodName;
			fqMethodName = XQueryMixinModel.PREFIX_FN + ":" + fqMethodName;
		} else {
			prefix = fqMethodName.substring(0, prefixPos);
			methodName = fqMethodName.substring(prefixPos + 1);
		}

		try {

			final LibDocument doc = XQueryMixinModel.getInstance()
					.getLibDocument(prefix);

			if (doc != null) {

				final int line = doc.getLineOfMethod(fqMethodName, argCount);
				if (line != -1) {

					final ExternalStorageEditorInput editorInput = new ExternalStorageEditorInput(
							new StringStorage(prefix + " (built-in)", doc
									.getPath(), doc.getContent().toString()));

					isLib[0] = new ForeignElement(parent, fqMethodName, caret) {
						public void codeSelect() {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {

									try {
										ITextEditor editor = (ITextEditor) PlatformUI
												.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage().openEditor(
														editorInput,
														XQueryEditor.EDITOR_ID);

										selectLibraryFunction(doc, line,
												methodName, editor);

									} catch (PartInitException e) {
										throw new RuntimeException(e);
									}

								}
							});
						}
					};
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return isLib[0];
	}

	private int parseArgCount(String source, int pos) {

		int c = 0;

		int bOpen = source.indexOf('(', pos);
		if (bOpen != -1) {
			int nOpen = 1;
			int bPos = bOpen + 1;
			while (bPos < source.length()) {
				char it = source.charAt(bPos);
				if (it == ')') {
					nOpen--;
				} else if (it == '(') {
					nOpen++;
				} else if (it == ',' && nOpen == 1) {
					c++;
				} else if (it != ' ' && c == 0) {
					c = 1;
				}
				if (nOpen == 0) {
					break;
				}
				bPos++;
			}

		}
		return c;
	}

	private boolean isCoveringFunction(IMethod fun, int offset) {
		try {
			ISourceRange sr = fun.getSourceRange();
			int declOff = sr.getOffset();
			int declLen = sr.getLength();
			int declEnd = declOff + declLen;

			int start = fun.getNameRange().getOffset();

			return start < offset && declEnd > offset;
		} catch (ModelException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean selectLibraryFunction(LibDocument doc, int line,
			final String method, ITextEditor editor) {

		try {
			IDocument document = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());

			int off = document.getLineOffset(line);

			if (off != -1) {
				editor.selectAndReveal(off, method.length());
				return true;
			}

		} catch (BadLocationException e) {
			// do nothing
		}
		return false;
	}
}
