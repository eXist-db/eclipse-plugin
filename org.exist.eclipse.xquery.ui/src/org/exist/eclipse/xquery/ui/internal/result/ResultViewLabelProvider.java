/**
 * ViewLabelProvider.java
 */
package org.exist.eclipse.xquery.ui.internal.result;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * The responsibility of this class is the presentation of the table from the
 * {@link ResultViewPart}.
 * 
 * @author Pascal Schmidiger
 */
public class ResultViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ResultItem resultItem = ResultItem.class.cast(element);
		if (columnIndex == 0) {
			return "" + (resultItem.getIndex() + 1);
		} else if (columnIndex == 1) {
			return resultItem.getContent();
		}
		return element.toString();
	}
}
