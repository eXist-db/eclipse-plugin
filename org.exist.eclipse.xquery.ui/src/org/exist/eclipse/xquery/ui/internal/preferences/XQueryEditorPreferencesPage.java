package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.EditorConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * The editor preference page.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryEditorPreferencesPage extends
		AbstractConfigurationBlockPreferencePage {

	protected String getHelpId() {
		return "";
	}

	protected void setDescription() {
		setDescription("Appeara&nce");
	}

	protected Label createDescriptionLabel(Composite parent) {
		return null;
	}

	protected void setPreferenceStore() {
		setPreferenceStore(XQueryUI.getDefault().getPreferenceStore());
	}

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new EditorConfigurationBlock(this, overlayPreferenceStore);
	}
}
