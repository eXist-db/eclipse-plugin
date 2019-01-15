/**
 * 
 */
package org.exist.eclipse.auto.internal.result.control;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.exist.eclipse.auto.internal.result.model.RunEntity;

/**
 * Provides the data for the table where the single runs are shown.
 * 
 * @author Markus Tanner
 */
public class RunsContentProvider implements ILazyContentProvider {

	private RunEntity[] _runEntities;
	private TableViewer _viewer;

	/**
	 * RunsContentProvider Constructor
	 */
	public RunsContentProvider() {
	}

	@Override
	public void dispose() {
		_runEntities = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		_viewer = (TableViewer) viewer;
		if (newInput != null) {
			_runEntities = (RunEntity[]) newInput;
		} else {
			_runEntities = new RunEntity[0];
		}
	}

	@Override
	public void updateElement(int index) {
		_viewer.replace(_runEntities[index], index);
	}
}
