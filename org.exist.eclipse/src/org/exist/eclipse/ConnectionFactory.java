/**
 * ConnectionFactory.java
 */
package org.exist.eclipse;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.internal.ConnectionBox;
import org.exist.eclipse.internal.ConnectionEnum;
import org.exist.eclipse.internal.wizards.LocalConnectionWizard;
import org.exist.eclipse.internal.wizards.NewConnectionWizard;
import org.exist.eclipse.internal.wizards.RemoteConnectionWizard;

/**
 * Factory to create a new connection.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ConnectionFactory {

	/**
	 * Create a new connection; it will open a new wizard.
	 * 
	 * @param workbench
	 */
	public static void createConnection(IWorkbench workbench)
			throws ConnectionException {
		NewConnectionWizard wizard = new NewConnectionWizard();
		wizard.init(workbench, null);
		wizard.setForcePreviousAndNextButtons(true);
		WizardDialog dialog = new WizardDialog(workbench.getDisplay()
				.getActiveShell(), wizard);
		dialog.open();
	}

	public static void changeConnection(IWorkbench workbench,
			IConnection connection, boolean copy) throws ConnectionException {
		if (connection.getType().equals(ConnectionEnum.remote)) {
			RemoteConnectionWizard wizard = new RemoteConnectionWizard();
			wizard.setConnection(connection, copy);
			wizard.init(workbench, null);
			WizardDialog dialog = new WizardDialog(workbench.getDisplay()
					.getActiveShell(), wizard);
			dialog.open();
		} else {
			LocalConnectionWizard wizard = new LocalConnectionWizard();
			wizard.setConnection(connection, copy);
			wizard.init(workbench, null);
			WizardDialog dialog = new WizardDialog(workbench.getDisplay()
					.getActiveShell(), wizard);
			dialog.open();
		}
	}

	public static IConnectionBox getConnectionBox() {
		return ConnectionBox.getInstance();
	}
}
