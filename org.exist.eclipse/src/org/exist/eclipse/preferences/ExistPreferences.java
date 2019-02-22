package org.exist.eclipse.preferences;

import java.nio.charset.Charset;

import org.eclipse.jface.preference.IPreferenceStore;
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
		IPreferenceStore prefs = BasePlugin.getDefault().getPreferenceStore();
		String encodingString = prefs.getString(IExistPreferenceConstants.PREFS_ENCODING);

		return Charset.forName(encodingString);
	}
}
