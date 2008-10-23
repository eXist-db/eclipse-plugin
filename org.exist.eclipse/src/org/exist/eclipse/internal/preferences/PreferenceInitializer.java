package org.exist.eclipse.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.exist.eclipse.internal.BasePlugin;

/**
 * Initialize the preferences on the first call.
 * 
 * @author Markus Tanner
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = doGetPreferenceStore();

		// Only initialize again if not yet initialized.
		// Otherwise preference.ini and plugin_customization.ini
		// wouldn't work.
		if (store.getDefaultString("EXIST_PLUGIN").length() == 0) {
			initializePublicPreferences(store);
		}
	}

	/**
	 * Gets the PreferenceStore of the current plugin
	 * 
	 * @return Plugin-Preferencestore
	 */
	protected IPreferenceStore doGetPreferenceStore() {
		return BasePlugin.getDefault().getPreferenceStore();
	}

	/**
	 * public configuration data
	 * 
	 * @param store -
	 *            die PreferenceStore-instance
	 */
	protected void initializePublicPreferences(IPreferenceStore store) {
		// encoding
		store.setDefault(IExistPreferenceConstants.PREFS_ENCODING,
				IExistPreferenceConstants.PREFS_ENCODING_DEFAULT);

	}

}
