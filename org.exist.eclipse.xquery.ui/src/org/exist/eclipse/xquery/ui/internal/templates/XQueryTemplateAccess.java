package org.exist.eclipse.xquery.ui.internal.templates;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.jface.preference.IPreferenceStore;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Provides access to the XQuery template store.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryTemplateAccess extends ScriptTemplateAccess {

	private static final String CUSTOM_TEMPLATES_KEY = "org.exist.eclipse.xquery.Templates";

	private static XQueryTemplateAccess instance;

	public static XQueryTemplateAccess getInstance() {
		if (instance == null) {
			instance = new XQueryTemplateAccess();
		}

		return instance;
	}

	protected String getContextTypeId() {
		return XQueryUniversalTemplateContextType.CONTEXT_TYPE_ID;
	}

	protected String getCustomTemplatesKey() {
		return CUSTOM_TEMPLATES_KEY;
	}

	protected IPreferenceStore getPreferenceStore() {
		return XQueryUI.getDefault().getPreferenceStore();
	}

}
