package org.exist.eclipse.preferences;

import java.nio.charset.Charset;

import org.eclipse.core.runtime.Preferences;
import org.exist.eclipse.internal.BasePlugin;
import org.exist.eclipse.internal.preferences.IExistPreferenceConstants;

/**
 * Class which contains the plugin preferences.
 * 
 * @author Markus Tanner
 */
public class ExistPreferences {

	/**
	 * @return the encoding which was defined in the preferences.
	 */
	public static Charset getEncoding() {
		Preferences prefs = BasePlugin.getDefault().getPluginPreferences();
		String encodingString = prefs
				.getString(IExistPreferenceConstants.PREFS_ENCODING);

		return Charset.forName(encodingString);
	}
}
