package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.EditorsUI;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryColorConstants;

/**
 * Initialize the preferences.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryUIPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = XQueryUI.getDefault().getPreferenceStore();

		EditorsUI.useAnnotationsPreferencePage(store);
		EditorsUI.useQuickDiffPreferencePage(store);

		// Initialize DLTK default values
		PreferenceConstants.initializeDefaultValues(store);

		// Initialize xquery constants
		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_COMMENT, new RGB(0, 192, 64));
		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_KEYWORD, new RGB(127, 0, 85));
		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_STRING, new RGB(42, 0, 255));
		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_KEYWORD_RETURN, new RGB(192, 192, 192));

		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION, new RGB(0, 0, 0));

		PreferenceConverter.setDefault(store, IXQueryColorConstants.XQUERY_ARGUMENT, new RGB(192, 0, 0));

		store.setDefault(IXQueryColorConstants.XQUERY_COMMENT + PreferenceConstants.EDITOR_BOLD_SUFFIX, false);
		store.setDefault(IXQueryColorConstants.XQUERY_COMMENT + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

		store.setDefault(IXQueryColorConstants.XQUERY_KEYWORD + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		store.setDefault(IXQueryColorConstants.XQUERY_KEYWORD + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

		store.setDefault(IXQueryColorConstants.XQUERY_KEYWORD_RETURN + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		store.setDefault(IXQueryColorConstants.XQUERY_KEYWORD_RETURN + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

		store.setDefault(IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION + PreferenceConstants.EDITOR_BOLD_SUFFIX,
				false);
		store.setDefault(IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION + PreferenceConstants.EDITOR_ITALIC_SUFFIX,
				false);

		store.setDefault(IXQueryColorConstants.XQUERY_ARGUMENT + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		store.setDefault(IXQueryColorConstants.XQUERY_ARGUMENT + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);

		store.setDefault(PreferenceConstants.EDITOR_TAB_WIDTH, 2);
		store.setDefault(PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, true);

		// folding
		store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);
		store.setDefault(PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED, true);

		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_CHAR, CodeFormatterConstants.TAB);
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, "2");
		store.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE, "2");

		store.setDefault(PreferenceConstants.CODEASSIST_AUTOACTIVATION_TRIGGERS, ".");
	}
}
