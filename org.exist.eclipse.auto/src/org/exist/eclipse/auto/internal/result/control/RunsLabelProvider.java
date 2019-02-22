/**
 * 
 */
package org.exist.eclipse.auto.internal.result.control;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.auto.internal.result.model.RunEntity;

/**
 * Provides the labels for the table of the single runs.
 * 
 * @author Markus Tanner
 */
public class RunsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RunEntity) {
			RunEntity entity = (RunEntity) element;
			if (columnIndex == 0) {
				return Integer.toString(entity.getIndex());
			} else if (columnIndex == 1) {
				return entity.getState().toString();
			} else if (columnIndex == 2) {
				return Integer.toString(entity.getCompilation()) + " ms";
			} else if (columnIndex == 3) {
				return Integer.toString(entity.getExecution()) + " ms";
			}

		}
		return "---";
	}

}
