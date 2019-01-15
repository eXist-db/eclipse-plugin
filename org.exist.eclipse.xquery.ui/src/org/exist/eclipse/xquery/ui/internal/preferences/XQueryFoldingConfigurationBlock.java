package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.preferences.FoldingConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.IFoldingPreferenceBlock;
import org.eclipse.jface.preference.PreferencePage;
import org.exist.eclipse.xquery.ui.internal.folding.XQueryFoldingPreferenceBlock;

/**
 * Block for setting the folding options.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryFoldingConfigurationBlock extends FoldingConfigurationBlock {

	public XQueryFoldingConfigurationBlock(OverlayPreferenceStore store,
			PreferencePage p) {
		super(store, p);
	}

	@Override
	protected IFoldingPreferenceBlock createFoldingPreferenceBlock() {
		return new XQueryFoldingPreferenceBlock(fStore);
	}
}
