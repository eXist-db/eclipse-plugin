package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Preferences for content assist.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryContentAssistPreference extends ContentAssistPreference {

	private static XQueryContentAssistPreference _instance;

	public static ContentAssistPreference getDefault() {
		if (_instance == null) {
			_instance = new XQueryContentAssistPreference();
		}
		return _instance;
	}

	@Override
	protected ScriptTextTools getTextTools() {
		return XQueryUI.getDefault().getTextTools();
	}
}
