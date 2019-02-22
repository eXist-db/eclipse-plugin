/**
 * 
 */
package org.exist.eclipse.auto.internal.result.control;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.auto.internal.result.model.QueryResultEntity;

/**
 * This class puts together the labels in the query-result table.
 * 
 * @author Markus Tanner
 */
public class ResultLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof QueryResultEntity) {
			QueryResultEntity entity = (QueryResultEntity) element;
			if (columnIndex == 0) {
				return entity.getName();
			} else if (columnIndex == 1) {
				return Integer.toString(entity.getAvgCompTime()) + " ms";
			} else if (columnIndex == 2) {
				return Integer.toString(entity.getAvgExecutionTime()) + " ms";
			}
		}
		return "---";
	}

}
