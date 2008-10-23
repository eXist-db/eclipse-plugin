package org.exist.eclipse.xquery.ui.internal.result;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * This view shows the xquery running results.
 * 
 * @author Pascal Schmidiger
 */
public class ResultView extends ViewPart {
	public static final String ID = "org.exist.eclipse.xquery.ui.internal.result.ResultView";
	private ResultViewPart _defaultPart;
	public static AtomicInteger _uniqueNr = new AtomicInteger(0);
	private TabFolder _tabFolder;
	private TabItem _tabItem;

	public ResultView() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);
		_tabFolder = new TabFolder(parent, SWT.TOP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		_tabFolder.setLayoutData(gd);
		_tabFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TabFolder tab = (TabFolder) e.getSource();
				TabItem[] items = tab.getSelection();
				for (TabItem item : items) {
					if (!item.equals(_tabItem)) {
						item.dispose();
					}
				}
			}
		});
		_tabItem = new TabItem(_tabFolder, SWT.NONE);
		_tabItem.setText("default");
		Composite composite = new Composite(_tabFolder, SWT.NONE);
		_defaultPart = new ResultViewPart();
		_defaultPart.createPartControl(composite);
		_tabItem.setControl(composite);
	}

	@Override
	public void setFocus() {

	}

	/**
	 * @param creation
	 *            information for the {@link IQueryFrame}.
	 * @return a new {@link IQueryFrame} in which you can run the xquery.
	 */
	public IQueryFrame createQueryFrame(IQueryFrameInfo creation) {
		int uniqueNr = getUniqueNr();
		ResultViewPart part = getResultViewPart(uniqueNr, creation
				.isCreatedNewTab());
		part.initCreation(creation, uniqueNr);
		return part;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private mehtods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private int getUniqueNr() {
		return _uniqueNr.getAndIncrement();
	}

	private ResultViewPart getResultViewPart(int uniqueNr, boolean withTabs) {
		ResultViewPart part = null;
		if (withTabs) {
			TabItem tabItem = new TabItem(_tabFolder, SWT.NONE);
			tabItem.setText("" + uniqueNr);
			part = new ResultViewPart();
			Composite composite = new Composite(_tabFolder, SWT.NONE);
			part.createPartControl(composite);
			tabItem.setControl(composite);
			_tabFolder.setSelection(_tabFolder.getItemCount() - 1);
		} else {
			_tabItem.setText("default_" + uniqueNr);
			_tabFolder.setSelection(0);
			part = _defaultPart;
		}
		return part;
	}
}