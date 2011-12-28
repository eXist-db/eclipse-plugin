package org.exist.eclipse;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * UI otils.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class UiUtil {
	/**
	 * See {@link MessageDialog#openConfirm(Shell, String, String)}, but with
	 * the 'modern' OK button named with action being taken.
	 */
	public static boolean openConfirm(Shell parent, String title,
			String message, String okButtonLabel) {
		MessageDialog dialog = new MessageDialog(parent, title, null, message,
				MessageDialog.QUESTION, new String[] { okButtonLabel,
						IDialogConstants.CANCEL_LABEL }, 0);
		return dialog.open() == 0;
	}
}
