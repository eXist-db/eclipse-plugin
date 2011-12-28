/**
 * OnShowEnum.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.exist.eclipse.IConnection;

/**
 * This class find out, on which context menu grade the extension could show.
 * 
 * @author Pascal Schmidiger
 * 
 */
public enum StateEnum {
	both, open, close;

	public boolean eval(IConnection[] selection) {
		boolean result = true;
		switch (this) {
		case open:
			for (IConnection item : selection) {
				if (!item.isOpen()) {
					result = false;
					break;
				}
			}
			break;
		case close:
			for (IConnection item : selection) {
				if (item.isOpen()) {
					result = false;
					break;
				}
			}
			break;
		case both:
			break;
		}

		return result;
	}
}
