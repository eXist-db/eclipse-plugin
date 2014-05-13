/**
 * TreeInvisible.java
 */
package org.exist.eclipse.browse.internal.browse;

import java.util.ArrayList;
import java.util.Collection;

import org.exist.eclipse.browse.browse.IBrowseItem;

/**
 * The root element of the tree is invisible and has only one element; the real
 * root element.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class BrowseItemInvisible extends BrowseItem {

	private Collection<BrowseItem> _children;

	public BrowseItemInvisible() {
		super(null, "");
		_children = new ArrayList<BrowseItem>();
	}

	/**
	 * Add the given child to the invisible root.
	 * 
	 * @param root
	 */
	public void addChild(BrowseItem root) {
		root.setParent(this);
		_children.add(root);
	}

	public IBrowseItem[] getChildren() {
		return _children.toArray(new BrowseItem[_children.size()]);
	}

	@Override
	public boolean hasChildren() {
		return _children.size() > 0;
	}
}
