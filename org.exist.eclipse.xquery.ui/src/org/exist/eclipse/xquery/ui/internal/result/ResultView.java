package org.exist.eclipse.xquery.ui.internal.result;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.exist.eclipse.xquery.ui.internal.result.ResultViewPart.PartFrame;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * This view shows the xquery running results.
 * 
 * @author Pascal Schmidiger
 */
public class ResultView extends ViewPart {
	public static final String ID = "org.exist.eclipse.xquery.ui.internal.result.ResultView";
	public static final AtomicInteger _uniqueNr = new AtomicInteger(0);
	private CTabFolder _tabFolder;
	private DisposeListener _tabItemDisposeListener;

	public ResultView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginTop = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		_tabFolder = new CTabFolder(parent, SWT.NONE);
		_tabFolder.setUnselectedCloseVisible(false);
		_tabFolder.marginWidth = -2;
		_tabFolder.marginHeight = -2;
		GridData gd = new GridData(GridData.FILL_BOTH);
		_tabFolder.setLayoutData(gd);
		CTabItem tabItem = createTabItem();
		setTabItemText(tabItem, "Result", -1);

		getViewSite().getActionBars().setGlobalActionHandler(
				ActionFactory.COPY.getId(), new Action() {
					@Override
					public void run() {
						CTabItem sel = _tabFolder.getSelection();
						PartFrame partFrame = (PartFrame) sel.getControl();
						new CopyResultItemsAction(partFrame.getPart()
								.getViewer()).run();
					}
				});
		_tabFolder.setSelection(0);
	}

	private DisposeListener getTabItemDisposeListener() {
		if (_tabItemDisposeListener == null) {
			_tabItemDisposeListener = new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					// do not let close last tab item
					if (_tabFolder.getItemCount() == 1) {
						_tabFolder.getItem(0).setShowClose(false);
					}
				}
			};
		}
		return _tabItemDisposeListener;
	}

	@Override
	public void setFocus() {
		_tabFolder.setFocus();
	}

	/**
	 * @param creation
	 *            information for the {@link IQueryFrame}.
	 * @return a new {@link IQueryFrame} in which you can run the xquery.
	 */
	public IQueryFrame createQueryFrame(IQueryFrameInfo creation) {
		int uniqueNr = nextUniqueNr();
		ResultViewPart part = getResultViewPart(uniqueNr, creation
				.getFilename(), creation.isCreatedNewTab());
		part.initCreation(creation, uniqueNr);

		return part;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private mehtods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private int nextUniqueNr() {
		return _uniqueNr.getAndIncrement();
	}

	private ResultViewPart getResultViewPart(int uniqueNr, String fileName,
			boolean withTabs) {
		CTabItem item;
		if (withTabs) {
			item = createTabItem();
			for (CTabItem it : _tabFolder.getItems()) {
				it.setShowClose(true); // has more than 1 now
			}
		} else {
			if (_tabFolder.getItemCount() == 0) {
				item = createTabItem();
			} else {
				item = _tabFolder.getSelection();
				if (item == null) {
					item = _tabFolder.getItem(0);
				}
			}
		}

		_tabFolder.setSelection(item);
		setTabItemText(item, fileName, uniqueNr);

		return ((PartFrame) item.getControl()).getPart();
	}

	private CTabItem createTabItem() {
		CTabItem tabItem = new CTabItem(_tabFolder, SWT.NONE);
		tabItem.addDisposeListener(getTabItemDisposeListener());
		ResultViewPart part = new ResultViewPart();
		PartFrame composite = new PartFrame(_tabFolder);
		part.createPartControl(composite);
		tabItem.setControl(composite);
		return tabItem;
	}

	private void setTabItemText(CTabItem tabItem, String fileName, int uniqueNr) {
		String text = fileName;
		if (uniqueNr != -1) {
			text += " #" + uniqueNr;
		}
		tabItem.setText(text);
	}
}