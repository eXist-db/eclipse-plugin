/**
 * QueryViewComposer.java
 */
package org.exist.eclipse.query.internal.views;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.query.internal.QueryPlugin;
import org.exist.eclipse.query.internal.control.IQueryInit;
import org.exist.eclipse.query.internal.proc.IQueryProcessor;
import org.exist.eclipse.query.internal.proc.QueryProcessor;

/**
 * This class puts the query view together.
 * 
 * @author Markus Tanner
 * @uml.dependency 
 *                 supplier="org.exist.eclipse.query.internal.views.TopContainer"
 * @uml.dependency 
 *                 supplier="org.exist.eclipse.query.internal.views.BodyContainer"
 */
public class BaseContainer implements IQueryInit {

	private Composite _parent;
	private QueryView _queryView;
	private TopContainer _topContainer;
	private BodyContainer _bodyContainer;
	private BottomContainer _bottomContainer;

	/**
	 * Constuctor
	 * 
	 * @param parent
	 */
	public BaseContainer(Composite parent, QueryView queryView) {
		_parent = parent;
		_queryView = queryView;

		_parent.addControlListener(new ControlListener() {
			@Override
			public void controlMoved(ControlEvent e) {
			}

			@Override
			public void controlResized(ControlEvent e) {
				// refreshView();
			}
		});
	}

	/**
	 * This method puts the assembles the query view. There are 4 composite
	 * parts that represent the query view:
	 * 
	 * base composite top composite body composite bottom composite
	 * 
	 * The base composite represents the main composite, on which the other
	 * composites are added to.
	 */
	public void composeView() {

		// base composite
		Composite baseContainer = new Composite(_parent, SWT.NULL);
		GridLayout baseLayout = new GridLayout();
		baseContainer.setLayout(baseLayout);

		// top composite
		_topContainer = new TopContainer(baseContainer, SWT.NULL, this);
		_topContainer.init();

		// body composite
		_bodyContainer = new BodyContainer(baseContainer, SWT.NULL, _queryView,
				this);
		_bodyContainer.init();

		// bottom composite
		_bottomContainer = new BottomContainer(baseContainer, SWT.NULL);
		_bottomContainer.init();
	}

	/**
	 * Disposes the content of the BaseContainer.
	 */
	public void dispose() {
		_topContainer.dispose();
		_bodyContainer.dispose();
		_bottomContainer.dispose();
	}

	@Override
	public void triggerQuery() {
		IBrowseItem item = _topContainer.getContextSelection();
		int maxDisplay = _topContainer.getMaxDisplay();
		String query = _bodyContainer.getQueryInput();
		if (query.length() < 1) {
			QueryPlugin.getDefault().infoDialog("eXist",
					"Don't forget to enter a query!");
		} else {
			IQueryProcessor queryProcessor = new QueryProcessor(item, query,
					maxDisplay);
			try {
				queryProcessor.runQuery();
			} catch (ConnectionException e) {
				String message = "Error while running query";
				Status status = new Status(IStatus.ERROR, QueryPlugin.getId(),
						message, e);
				QueryPlugin.getDefault().errorDialog(message, e.getMessage(),
						status);
			}
		}
	}

	public void setFocus() {
		_bodyContainer.setFocus();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////////////////////////

}
