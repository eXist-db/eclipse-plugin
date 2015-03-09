package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Wizard for creating a new XQuery project.
 * 
 * @author Patrick Reinhart
 */
public class NewXQueryProjectWizard extends BasicNewProjectResourceWizard {
	private static final String[] EXIST_NATURE = new String[] { "org.exist.eclipse.xquery.core.nature" };

	@Override
	public boolean performFinish() {
		boolean performFinish = super.performFinish();
		if (performFinish) {
			try {
				IProject newProject = getNewProject();
				IProjectDescription description = newProject.getDescription();
				description.setNatureIds(EXIST_NATURE);
				newProject.setDescription(description, new NullProgressMonitor());
			} catch (CoreException e) {
				XQueryUI.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID,
								"Unable to add nature", e));
				return false;
			}
		}
		return performFinish;
	}
}