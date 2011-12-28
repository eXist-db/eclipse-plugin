package org.exist.eclipse.xquery.ui.internal.result;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Copies selected ResultItems to clipboard.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class CopyResultItemsAction extends Action {

	/**
	 * @return true if successful
	 */
	public static boolean copyToClipboard(String text) {
		if (text.isEmpty()) {
			return true;
		}
		Clipboard cb = null;
		boolean ok = false;
		try {
			cb = new Clipboard(Display.getDefault());
			cb.setContents(new Object[] { text },
					new TextTransfer[] { TextTransfer.getInstance() });
			ok = true;
		} finally {
			if (cb != null) {
				cb.dispose();
			}
		}
		return ok;
	}

	private final TableViewer _viewer;

	public CopyResultItemsAction(TableViewer viewer) {
		super("Copy", PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		_viewer = viewer;
	}

	@Override
	public void run() {
		copyToClipboard(getCopyContent());
	}

	public String getCopyContent() {
		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);
		Object[] all = ((IStructuredSelection) _viewer.getSelection())
				.toArray();

		for (Object it : all) {
			ResultItem resultItem = (ResultItem) it;
			pw.println(resultItem.getContent());
		}

		String content = out.toString();
		return content;
	}
}
