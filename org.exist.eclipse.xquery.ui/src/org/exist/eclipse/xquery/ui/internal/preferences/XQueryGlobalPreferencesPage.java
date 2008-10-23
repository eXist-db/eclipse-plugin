package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * The global xquery preference page.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryGlobalPreferencesPage extends
		AbstractConfigurationBlockPreferencePage {

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new XQueryGlobalConfigurationBlock(overlayPreferenceStore, this);
	}

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		setDescription("General XQuery settings");
	}

	protected void setPreferenceStore() {
		setPreferenceStore(XQueryUI.getDefault().getPreferenceStore());
	}

}
