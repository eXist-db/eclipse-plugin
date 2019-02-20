package org.exist.eclipse.query.internal.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This view enables the user to send XQueries to the database.
 * 
 * @author Markus Tanner
 */

public class QueryView extends ViewPart {
	public static final String ID = "org.exist.eclipse.query.internal.views.QueryView";
	private BaseContainer _composer;

	/**
	 * The constructor.
	 */
	public QueryView() {
	}

	@Override
	public void dispose() {
		if (_composer != null) {
			_composer.dispose();
		}
		super.dispose();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		_composer = new BaseContainer(parent, this);
		_composer.composeView();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		_composer.setFocus();
	}

}