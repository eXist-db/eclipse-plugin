package org.exist.eclipse.query.internal.xquery.context;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseItemListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.listener.ConnectionRegistration;
import org.exist.eclipse.listener.IConnectionListener;
import org.exist.eclipse.query.internal.xquery.completion.CompletionExtension;
import org.exist.eclipse.query.internal.xquery.completion.GetFunctionJob;
import org.exist.eclipse.query.internal.xquery.result.QueryJob;
import org.exist.eclipse.xquery.ui.completion.ICompletionExtension;
import org.exist.eclipse.xquery.ui.context.ConnectionContextEvent;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.context.IContextListener;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * This class represents the eXist {@link IConnectionContext}. It holds also the
 * {@link IContextListener}. For information about the context state, this class
 * uses the {@link IConnectionListener} and the {@link IBrowseItemListener}
 * interfaces.
 * 
 * @author Pascal Schmidiger
 */
public class ConnectionContext implements IConnectionContext,
		IConnectionListener, IBrowseItemListener {

	private Collection<IContextListener> _listeners;
	private IBrowseItem _item;
	private final GetFunctionJob _functionJob;

	public ConnectionContext(IBrowseItem item, GetFunctionJob functionJob) {
		_item = item;
		_functionJob = functionJob;
		_listeners = new ArrayList<IContextListener>();
	}

	@Override
	public String getName() {
		return _item.getPath() + " (" + _item.getConnection().getName() + ")";
	}

	@Override
	public void addContextListener(IContextListener listener) {
		Assert.isNotNull(listener);
		if (_listeners.size() < 1) {
			ConnectionRegistration.addListener(this);
			BrowseCoordinator.getInstance().addListener(this);
		}
		_listeners.add(listener);
	}

	@Override
	public void removeContextListener(IContextListener listener) {
		Assert.isNotNull(listener);
		_listeners.remove(listener);
		if (_listeners.size() < 1) {
			ConnectionRegistration.removeListener(this);
			BrowseCoordinator.getInstance().removeListener(this);
		}
	}

	@Override
	public void run(IQueryFrame frame) {
		if (_item != null
				&& IManagementService.class.cast(
						_item.getConnection().getAdapter(
								IManagementService.class)).check()) {
			if (IBrowseService.class.cast(
					_item.getAdapter(IBrowseService.class)).check()) {
				QueryJob job = new QueryJob(frame, _item);
				job.setUser(true);
				job.schedule();
			}
		}
	}

	@Override
	public ICompletionExtension getCompletionExtension() {
		if (_item != null
				&& IManagementService.class.cast(
						_item.getConnection().getAdapter(
								IManagementService.class)).check()) {
			if (IBrowseService.class.cast(
					_item.getAdapter(IBrowseService.class)).check()) {
				return new CompletionExtension(_functionJob);
			}
		}
		return null;
	}

	@Override
	public void added(IConnection connection) {

	}

	@Override
	public void closed(IConnection connection) {
		if (_item.getConnection().equals(connection)) {
			fireDisposed();
		}
	}

	@Override
	public void opened(IConnection connection) {

	}

	@Override
	public void removed(IConnection connection) {
	}

	@Override
	public void added(IBrowseItem item) {
		// do nothing
	}

	@Override
	public void moved(IBrowseItem fromItem, IBrowseItem toItem) {
		if (_item.equals(fromItem)) {
			_item = toItem;
			fireRefresh();
		}
	}

	@Override
	public void refresh(IBrowseItem item) {
		if (_item.equals(item)) {
			fireRefresh();
		}
	}

	@Override
	public void removed(IBrowseItem[] items) {
		for (IBrowseItem item : items) {
			if (_item.equals(item)) {
				fireDisposed();
				break;
			}
		}

	}

	private void fireDisposed() {
		final ConnectionContextEvent event = new ConnectionContextEvent(this);
		final IContextListener[] listeners = _listeners
				.toArray(new IContextListener[_listeners.size()]);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				for (IContextListener listener : listeners) {
					listener.disposed(event);
				}
			}
		});
	}

	private void fireRefresh() {
		final ConnectionContextEvent event = new ConnectionContextEvent(this);
		final IContextListener[] listeners = _listeners
				.toArray(new IContextListener[_listeners.size()]);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				for (IContextListener listener : listeners) {
					listener.refresh(event);
				}
			}
		});
	}
}
