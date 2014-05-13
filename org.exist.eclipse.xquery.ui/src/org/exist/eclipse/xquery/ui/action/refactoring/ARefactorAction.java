package org.exist.eclipse.xquery.ui.action.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.RewriteSessionEditProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.text.edits.TextEdit;
import org.exist.eclipse.xquery.ui.action.AXQueryEditorAction;

abstract public class ARefactorAction extends AXQueryEditorAction {

	protected static boolean _lastReplaceAllButtonSelection = true;

	protected static Button createReplaceAllButton(Composite parent,
			final int nOccurences, final boolean[] replaceAll) {
		final Button replaceButton = new Button(parent, SWT.CHECK);
		replaceButton.setText("&Replace all occurences (" + nOccurences + ")");
		replaceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				replaceAll[0] = replaceButton.getSelection();
			}
		});
		replaceButton.setSelection(_lastReplaceAllButtonSelection);
		return replaceButton;
	}

	protected void applyEdits(IDocument document, TextEdit edits) {
		try {
			// RewriteSessionEditProcessor undo all at once
			// see
			// http://www.eclipse.org/forums/index.php/m/319909/?srch=MultiTextEdit#msg_319909
			new RewriteSessionEditProcessor(document, edits,
					TextEdit.CREATE_UNDO | TextEdit.UPDATE_REGIONS)
					.performEdits();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected List<Integer> findOffsets(IDocument document, String text) {
		List<Integer> offs = new ArrayList<Integer>();
		int pos = 0;
		String all = document.get();
		while (true) {
			pos = all.indexOf(text, pos);
			if (pos == -1) {
				break;
			}
			offs.add(Integer.valueOf(pos));
			pos += text.length() + 1;
		}
		return offs;
	}

	protected String getOneTabOrTwoSpaces() {
		// TODO:see TextEditor settings
		return "\t";
	}
}
