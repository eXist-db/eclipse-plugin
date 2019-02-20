package org.exist.eclipse.xquery.ui.action.refactoring;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * Extracts a method out of selection (text-like, not real AST transformation).
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class ExtractMethodAction extends ARefactorAction {

	public static final String ID = ExtractMethodAction.class.getName();

	public ExtractMethodAction() {
		setActionDefinitionId(ID);
	}

	@Override
	protected void doRun(XQueryEditor editor) {
		try {

			IDocument document = getDocument(editor);

			ISourceViewer viewer = editor.getViewer();
			Point range = viewer.getSelectedRange();

			int len = range.y;
			if (len == 0) {
				return;
			}

			int off = range.x;
			String sel = document.get(off, len);
			List<Integer> offsets = findOffsets(document, sel);

			int nOffs = offsets.size();

			boolean[] replaceAll = { _lastReplaceAllButtonSelection };
			String text = openMethodNameInputDialog(editor.getEditorSite().getShell(), nOffs, replaceAll);

			if (text == null) {
				return;
			}

			String methodContent = getSignature(text);
			methodContent += " {\n";
			methodContent += getOneTabOrTwoSpaces() + sel + "\n";
			methodContent += "};\n\n";

			int firstOff = offsets.get(0).intValue();
			int line = document.getLineOfOffset(firstOff);
			int lineOff = document.getLineOffset(line);

			MultiTextEdit mte = new MultiTextEdit();

			mte.addChild(new InsertEdit(lineOff, methodContent));

			String methodCall = parseMethodCall(text);
			for (Integer o : offsets) {
				mte.addChild(new ReplaceEdit(o.intValue(), len, methodCall));
				if (!replaceAll[0]) {
					break;
				}
			}

			// works better if sets range before apply edits
			viewer.getTextWidget().setSelectionRange(off, methodCall.length());
			applyEdits(document, mte);

		} catch (Exception e) {
			XQueryUI.getDefault().getLog().log(
					new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID, "An error occured while extracting method: " + e, e));
		}
	}

	/**
	 * @return nullable if canceled
	 */
	private String openMethodNameInputDialog(Shell shell, final int nOccurences, final boolean[] replaceAll) {
		final String defaultMethodName = "foo";

		// for the developer
		// String testSyntax = "($a as xs:string*, $c, $d) as xs:string";
		String testSyntax = "";

		final String defaultName = "local:" + defaultMethodName + testSyntax;

		InputDialog dialog = new InputDialog(shell, "Extract Method", "&Name or Signature", defaultName,
				new IInputValidator() {
					@Override
					public String isValid(String newText) {
						// not displayed; just disable ok button
						return newText.isEmpty() ? "" : null;
					}
				}) {
			@Override
			protected Control createContents(Composite container) {
				Control contents = super.createContents(container);

				Composite parent = (Composite) ((Composite) contents).getChildren()[0];

				final Label previewLabel = new Label(parent, SWT.NONE);
				previewLabel.setForeground(JFaceColors.getHyperlinkText(previewLabel.getDisplay()));
				previewLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				final Text text = (Text) parent.getChildren()[1];

				// hide status text
				parent.getChildren()[2].setLayoutData(new GridData(0, 0));

				final ModifyListener modifyListener = new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						String signature = getSignature(text.getText());
						if (signature.isEmpty()) {
							signature = "<preview>";
						}
						previewLabel.setText(signature);
					}
				};
				text.addModifyListener(modifyListener);

				final Button replaceButton = createReplaceAllButton(parent, nOccurences, replaceAll);

				GridData gd = new GridData(0);
				gd.verticalIndent = 10;
				replaceButton.setLayoutData(gd);

				parent.setTabList(new Control[] { text, replaceButton });

				text.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						text.setSelection(defaultName.length() - defaultMethodName.length(), defaultName.length());
						modifyListener.modifyText(null);
					}
				});

				return contents;
			}
		};

		int result = dialog.open();
		return (result == 0) ? dialog.getValue() : null;
	}

	private String getSignature(String text) {
		if (text.isEmpty()) {
			return "";
		}
		String s = "declare function " + text;
		if (text.indexOf('(') == -1) {
			s += "()";
		}
		return s;
	}

	private String parseMethodCall(String text) {
		String result;
		int pos = text.indexOf('(');
		if (pos == -1) {
			result = text + "()";
		} else {
			int posEnd = text.lastIndexOf(')');
			if (posEnd == -1) {
				posEnd = text.length() - 1;
			}
			result = text.substring(0, pos);
			String params = text.substring(pos + 1, posEnd);
			String[] splits = params.split(",");
			result += "(";
			String args = "";
			for (String v : splits) {
				if (!args.isEmpty()) {
					args += ", ";
				}
				args += parseVar(v);
			}
			result += args;
			result += ")";
		}

		return result;
	}

	private String parseVar(String text) {
		text = text.trim();
		String var;

		// remove type stuff
		int pos = text.indexOf(' ');
		if (pos == -1) {
			var = text;
		} else {
			var = text.substring(0, pos);
		}

		return var.trim();
	}

}
