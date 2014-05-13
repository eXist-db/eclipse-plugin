package org.exist.eclipse.xquery.ui.action.refactoring;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * Extracts local variable and replaces occurences (text search/replace like,
 * not real AST transformation).
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class ExtractLocalVariableAction extends ARefactorAction {

	public static final String ID = ExtractLocalVariableAction.class.getName();

	public ExtractLocalVariableAction() {
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

			String sel = document.get(range.x, len);

			List<Integer> offsets = findOffsets(document, sel);

			int nOffs = offsets.size();

			boolean[] replaceAll = { _lastReplaceAllButtonSelection };
			String varName = openVariableNameInputDialog(editor
					.getEditorSite().getShell(), nOffs, replaceAll);
			if (varName == null) {
				return;
			}

			_lastReplaceAllButtonSelection = replaceAll[0];

			if (varName.charAt(0) != '$') {
				varName = '$' + varName;
			}

			int firstOff = offsets.get(0).intValue();
			int line = document.getLineOfOffset(firstOff);
			int lineOff = document.getLineOffset(line);

			MultiTextEdit mte = new MultiTextEdit();

			mte.addChild(new InsertEdit(lineOff, "let " + varName + " := "
					+ sel + "\n"));

			for (Integer o : offsets) {
				mte.addChild(new ReplaceEdit(o.intValue(), len, varName));
				if (!replaceAll[0]) {
					break;
				}
			}

			applyEdits(document, mte);

		} catch (Exception e) {
			XQueryUI.getDefault().getLog().log(
					new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID,
							"An error occured while extracting local variable: "
									+ e, e));
		}
	}

	/**
	 * @return nullable if canceled
	 */
	private String openVariableNameInputDialog(Shell shell,
			final int nOccurences, final boolean[] replaceAll) {
		InputDialog dialog = new InputDialog(shell, "Extract Local Variable",
				"&Name", "foo", new IInputValidator() {
					@Override
					public String isValid(String newText) {
						// not displayed; just disable ok button
						return newText.isEmpty() ? "" : null;
					}
				}) {
			@Override
			protected Control createContents(Composite container) {
				Control contents = super.createContents(container);

				Composite parent = (Composite) ((Composite) contents)
						.getChildren()[0];
				final Button replaceButton = createReplaceAllButton(parent,
						nOccurences, replaceAll);
				final Text text = (Text) parent.getChildren()[1];

				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						text.setToolTipText("adsdsds");
					}
				});

				// hide status text
				parent.getChildren()[2].setLayoutData(new GridData(0, 0));
				parent.setTabList(new Control[] { text, replaceButton });

				return contents;
			}
		};

		int result = dialog.open();
		return (result == 0) ? dialog.getValue() : null;
	}
}
