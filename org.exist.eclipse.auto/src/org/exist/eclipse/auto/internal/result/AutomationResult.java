/**
 * 
 */
package org.exist.eclipse.auto.internal.result;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.exist.eclipse.auto.internal.result.view.QueryGroup;
import org.exist.eclipse.auto.internal.run.AutomationHandler;
import org.exist.eclipse.auto.query.IQueryResult;

/**
 * The AutomationResult is the instance that collects all the results of the
 * single queries. If expected number of results is reached, an output is
 * generated.
 * 
 * @author Markus Tanner
 */
public class AutomationResult {
	private int _resultCount;
	private int _expectedCount;
	private int _threadCount;
	private String _target;
	private QueryOrderType _queryOrderType;
	private String _autoNote;

	private Map<Integer, QueryGroup> _results;
	private final IProgressMonitor _monitor;

	/**
	 * AutomationResult constructor
	 * 
	 * @param expectedCount
	 * @param threadCount
	 * @param type
	 * @param autoNote
	 * @param monitor
	 * @param target
	 */
	public AutomationResult(int expectedCount, int threadCount, QueryOrderType type, String autoNote,
			IProgressMonitor monitor, String target) {
		_expectedCount = expectedCount;
		_threadCount = threadCount;
		_queryOrderType = type;
		_autoNote = autoNote;
		_monitor = monitor;
		_target = target;

		_results = new HashMap<>(expectedCount);
		_resultCount = 0;
	}

	/**
	 * Method over which a result can be added.
	 * 
	 * @param result
	 */
	public synchronized void addQueryResult(IQueryResult result) {
		Integer key = Integer.valueOf(result.getQuery().getId());
		QueryGroup group = _results.get(key);
		if (group == null) {
			group = new QueryGroup(result.getQuery());
		}
		group.addResult(result);
		_results.put(key, group);

		_resultCount++;
		_monitor.worked(1);
		notify();
	}

	/**
	 * Waits until the expected amount of results is received or the automation is
	 * cancelled.
	 */
	public synchronized void join() {
		try {
			while (!_monitor.isCanceled() && _resultCount < _expectedCount) {
				wait();
			}
			if (_resultCount == _expectedCount) {
				displayResult();
			}
		} catch (InterruptedException e) {
			// ignore
		} finally {
			AutomationHandler.getInstance().cleanup();
		}
	}

	// -------------------------------------------------------------------------
	// Private Methods
	// -------------------------------------------------------------------------

	/**
	 * Displays the result
	 */
	private void displayResult() {
		ResultProcessor result = new ResultProcessor(_results, _threadCount, _queryOrderType, _autoNote,
				_expectedCount);

		final File file = new File(_target);
		try {
			result.writeResultXml(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				openEditor(file);
			}
		});
	}

	/**
	 * Open the automation result editor with the generated output. Because the
	 * document is not in the workspace, the file needs to be opened by the EFS.
	 * 
	 * @param fileToOpen
	 */
	private void openEditor(File fileToOpen) {
		if (fileToOpen.exists() && fileToOpen.isFile()) {

			final IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());

			IWorkbenchPage page = AutoUI.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null) {
				try {
					IDE.openEditorOnFileStore(page, fileStore);
				} catch (PartInitException e) {
					Status status = new Status(IStatus.ERROR, AutoUI.getId(), e.getMessage(), e);
					AutoUI.getDefault().getLog().log(status);
					ErrorDialog.openError(Display.getCurrent().getActiveShell(),
							"Open the automation result editor failed.", "Close the automation and open it again.",
							status);
				}
			}
		}
	}
}
