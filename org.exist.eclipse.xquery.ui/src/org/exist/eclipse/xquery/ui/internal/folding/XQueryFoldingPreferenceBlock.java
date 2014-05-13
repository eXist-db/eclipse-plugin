package org.exist.eclipse.xquery.ui.internal.folding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.internal.ui.text.folding.FoldingMessages;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore.OverlayKey;
import org.eclipse.dltk.ui.text.folding.IFoldingPreferenceBlock;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * XQuery default folding preferences.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryFoldingPreferenceBlock implements IFoldingPreferenceBlock {

	private OverlayPreferenceStore _overlayStore;
	private OverlayKey[] _keys;
	private Map<Button, String> _checkBoxes;

	public XQueryFoldingPreferenceBlock(OverlayPreferenceStore store) {
		_checkBoxes = new HashMap<Button, String>();
		_overlayStore = store;
		_keys = createKeys();
		_overlayStore.addKeys(_keys);
	}

	private OverlayKey[] createKeys() {
		ArrayList<OverlayPreferenceStore.OverlayKey> overlayKeys = new ArrayList<OverlayPreferenceStore.OverlayKey>();

		OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys
				.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	@Override
	public Control createControl(Composite composite) {
		_overlayStore.load();
		_overlayStore.start();

		Composite inner = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.verticalSpacing = 3;
		layout.marginWidth = 0;
		inner.setLayout(layout);

		Label label = new Label(inner, SWT.LEFT);
		label.setText(FoldingMessages.DefaultFoldingPreferenceBlock_title);

		return inner;
	}

	private void initializeFields() {
		for (Button b : _checkBoxes.keySet()) {
			String key = _checkBoxes.get(b);
			b.setSelection(_overlayStore.getBoolean(key));
		}
	}

	@Override
	public void performOk() {
		_overlayStore.propagate();
	}

	@Override
	public void initialize() {
		initializeFields();
	}

	@Override
	public void performDefaults() {
		_overlayStore.loadDefaults();
		initializeFields();
	}

	@Override
	public void dispose() {
		_overlayStore.stop();
	}
}
