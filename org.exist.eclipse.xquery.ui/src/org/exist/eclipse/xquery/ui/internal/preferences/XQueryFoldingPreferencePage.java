package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * The page for setting the editor options.
 * 
 * @author Pascal Schmidiger
 */
public final class XQueryFoldingPreferencePage extends
		AbstractConfigurationBlockPreferencePage {

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		String description = PreferencesMessages.EditorPreferencePage_folding_title;
		setDescription(description);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(XQueryUI.getDefault().getPreferenceStore());
	}

	protected Label createDescriptionLabel(Composite parent) {
		return null; // no description for new look.
	}

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new XQueryFoldingConfigurationBlock(overlayPreferenceStore, this);
	}
}
