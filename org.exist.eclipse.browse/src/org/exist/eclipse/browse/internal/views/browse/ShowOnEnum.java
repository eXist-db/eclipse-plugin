/**
 * OnShowEnum.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.exist.eclipse.browse.browse.IBrowseItem;

/**
 * This class find out, on which context menu grade the extension could show.
 * 
 * @author Pascal Schmidiger
 * 
 */
public enum ShowOnEnum {
	all, root, collection;

	public boolean eval(IBrowseItem[] selection) {
		boolean result = true;
		switch (this) {
		case root:
			for (IBrowseItem item : selection) {
				if (!item.isRoot()) {
					result = false;
					break;
				}
			}
			break;
		case collection:
			for (IBrowseItem item : selection) {
				if (item.isRoot()) {
					result = false;
					break;
				}
			}
			break;
		case all:
			break;
		}
		return result;
	}
}
