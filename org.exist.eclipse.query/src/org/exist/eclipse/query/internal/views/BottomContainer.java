package org.exist.eclipse.query.internal.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.exist.eclipse.query.internal.proc.IQueryListener;
import org.exist.eclipse.query.internal.proc.QueryEndEvent;
import org.exist.eclipse.query.internal.proc.QueryNotifier;
import org.exist.eclipse.query.internal.proc.QueryStartEvent;

/**
 * This class is responsible for the query view bottom.
 * 
 * @author Markus Tanner
 */
public class BottomContainer extends Composite implements IQueryListener {
	private Label _itemsFound;

	BottomContainer(Composite parent, int style) {
		super(parent, style);
	}

	public void init() {
		GridLayout bottomLayout = new GridLayout();
		bottomLayout.numColumns = 1;
		this.setLayout(bottomLayout);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		this.setLayoutData(gd);

		composeStats(this);

	}

	@Override
	public void dispose() {
		QueryNotifier.getInstance().removeListener(this);
		super.dispose();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Composes the lowest part of the query view.
	 * 
	 * @param contextContainer
	 */
	private void composeStats(Composite contextContainer) {
		// label control - items found
		_itemsFound = new Label(contextContainer, SWT.LEFT);
		_itemsFound.pack();

		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		_itemsFound.setLayoutData(gd);
		QueryNotifier.getInstance().addListener(this);
	}

	public void end(final QueryEndEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				StringBuilder text = new StringBuilder(100);
				text.append(event.getCount()).append(" items found | ");
				text.append("Compilation: ").append(event.getCompiledTime())
						.append(" ms | ");
				text.append("Execution: ").append(event.getExectionTime())
						.append(" ms");
				_itemsFound.setText(text.toString());
				_itemsFound.pack();
			}
		});
	}

	public void start(QueryStartEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				_itemsFound.setText("Query processing...");
				_itemsFound.pack();
			}
		});
	}

}
