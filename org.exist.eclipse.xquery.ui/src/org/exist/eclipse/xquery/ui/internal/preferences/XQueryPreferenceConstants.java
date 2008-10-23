package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.preferences.NewScriptProjectPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryColorConstants;

/**
 * All preferences constants which are defined in this project.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryPreferenceConstants extends PreferenceConstants {
	/**
	 * A named preference that holds the color used to render single line
	 * comments.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_COMMENT_COLOR = IXQueryColorConstants.XQUERY_COMMENT;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_COMMENT_BOLD = IXQueryColorConstants.XQUERY_COMMENT
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_COMMENT_ITALIC = IXQueryColorConstants.XQUERY_COMMENT
			+ EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in strikethrough. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_COMMENT_STRIKETHROUGH = IXQueryColorConstants.XQUERY_COMMENT
			+ EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in underline. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 * 
	 * 
	 */
	public final static String EDITOR_COMMENT_UNDERLINE = IXQueryColorConstants.XQUERY_COMMENT
			+ EDITOR_UNDERLINE_SUFFIX;

	/*
	 * Key worlds
	 */
	/**
	 * A named preference that holds the color used to render keyword.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_COLOR = IXQueryColorConstants.XQUERY_KEYWORD;

	/**
	 * A named preference that controls whether kwyword are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_BOLD = IXQueryColorConstants.XQUERY_KEYWORD
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_ITALIC = IXQueryColorConstants.XQUERY_KEYWORD
			+ EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in strikethrough. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_STRIKETHROUGH = IXQueryColorConstants.XQUERY_KEYWORD
			+ EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in
	 * underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in underline. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 * 
	 * 
	 */
	public final static String EDITOR_KEYWORD_UNDERLINE = IXQueryColorConstants.XQUERY_KEYWORD
			+ EDITOR_UNDERLINE_SUFFIX;
	/*
	 * keyword return color
	 */
	/**
	 * A named preference that holds the color used to render keyword.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_RETURN_COLOR = IXQueryColorConstants.XQUERY_KEYWORD_RETURN;

	/**
	 * A named preference that controls whether kwyword are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_BOLD = IXQueryColorConstants.XQUERY_KEYWORD_RETURN
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_ITALIC = IXQueryColorConstants.XQUERY_KEYWORD_RETURN
			+ EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in strikethrough. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_STRIKETHROUGH = IXQueryColorConstants.XQUERY_KEYWORD_RETURN
			+ EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in
	 * underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in underline. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 * 
	 * 
	 */
	public final static String EDITOR_KEYWORD_RETURN_UNDERLINE = IXQueryColorConstants.XQUERY_KEYWORD_RETURN
			+ EDITOR_UNDERLINE_SUFFIX;

	/*
	 * Numbers
	 */
	/**
	 * A named preference that holds the color used to render NUMBER.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_NUMBER_COLOR = IXQueryColorConstants.XQUERY_NUMBER;

	/**
	 * A named preference that controls whether number are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_BOLD = IXQueryColorConstants.XQUERY_NUMBER
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether NUMBER are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_ITALIC = IXQueryColorConstants.XQUERY_NUMBER
			+ EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in strikethrough. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_STRIKETHROUGH = IXQueryColorConstants.XQUERY_NUMBER
			+ EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether NUMBER are rendered in
	 * underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in underline. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 * 
	 * 
	 */

	public final static String EDITOR_NUMBER_UNDERLINE = IXQueryColorConstants.XQUERY_NUMBER
			+ EDITOR_UNDERLINE_SUFFIX;

	/*
	 * Strings
	 */
	/**
	 * A named preference that holds the color used to render STRING.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a
	 * string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_STRING_COLOR = IXQueryColorConstants.XQUERY_STRING;

	/**
	 * A named preference that controls whether STRING are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in bold. If <code>false</code> the are rendered
	 * using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_BOLD = IXQueryColorConstants.XQUERY_STRING
			+ EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether STRING are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in italic. If <code>false</code> the are rendered
	 * using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_ITALIC = IXQueryColorConstants.XQUERY_STRING
			+ EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are
	 * rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in strikethrough. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_STRIKETHROUGH = IXQueryColorConstants.XQUERY_STRING
			+ EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether STRING are rendered in
	 * underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line
	 * comments are rendered in underline. If <code>false</code> the are
	 * rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_UNDERLINE = IXQueryColorConstants.XQUERY_STRING
			+ EDITOR_UNDERLINE_SUFFIX;

	public final static String EDITOR_ARGUMENT_COLOR = IXQueryColorConstants.XQUERY_ARGUMENT;

	public final static String EDITOR_FUNCTION_DEFINITON_COLOR = IXQueryColorConstants.XQUERY_FUNCTION_DEFINITION;

	public static void initializeDefaultValues(IPreferenceStore store) {
		PreferenceConstants.initializeDefaultValues(store);

		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_COMMENT_COLOR, new RGB(63,
						127, 95));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_KEYWORD_COLOR, new RGB(127, 0,
						85));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_KEYWORD_RETURN_COLOR, new RGB(
						127, 0, 85));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_STRING_COLOR, new RGB(42, 0,
						255));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_NUMBER_COLOR, new RGB(128, 0,
						0));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_ARGUMENT_COLOR, new RGB(0, 0,
						0));
		PreferenceConverter.setDefault(store,
				XQueryPreferenceConstants.EDITOR_FUNCTION_DEFINITON_COLOR,
				new RGB(0, 0, 0));

		store.setDefault(XQueryPreferenceConstants.EDITOR_COMMENT_BOLD, false);
		store
				.setDefault(XQueryPreferenceConstants.EDITOR_COMMENT_ITALIC,
						false);

		store.setDefault(XQueryPreferenceConstants.EDITOR_KEYWORD_BOLD, true);
		store
				.setDefault(XQueryPreferenceConstants.EDITOR_KEYWORD_ITALIC,
						false);
		store.setDefault(XQueryPreferenceConstants.EDITOR_KEYWORD_RETURN_BOLD,
				true);
		store.setDefault(
				XQueryPreferenceConstants.EDITOR_KEYWORD_RETURN_ITALIC, false);

		store.setDefault(PreferenceConstants.EDITOR_SMART_INDENT, true);
		store.setDefault(PreferenceConstants.EDITOR_CLOSE_STRINGS, true);
		store.setDefault(PreferenceConstants.EDITOR_CLOSE_BRACKETS, true);
		store.setDefault(PreferenceConstants.EDITOR_CLOSE_BRACES, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_TAB, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_PASTE, true);
		store.setDefault(PreferenceConstants.EDITOR_SMART_HOME_END, true);
		store.setDefault(PreferenceConstants.EDITOR_SUB_WORD_NAVIGATION, true);
		store.setDefault(PreferenceConstants.EDITOR_TAB_WIDTH, 8);
		store.setDefault(
				PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, true);

		// folding
		store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);
		// store.setDefault(PreferenceConstants.EDITOR_FOLDING_INNERTYPES,
		// false);
		// store.setDefault(PreferenceConstants.EDITOR_FOLDING_METHODS, false);
		// store.setDefault(PreferenceConstants.EDITOR_FOLDING_IMPORTS, true);

		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_CHAR,
				CodeFormatterConstants.TAB);
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		store
				.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE,
						"4");

		NewScriptProjectPreferencePage.initDefaults(store);

		store.setDefault(PreferenceConstants.APPEARANCE_COMPRESS_PACKAGE_NAMES,
				false);
		store.setDefault(PreferenceConstants.APPEARANCE_METHOD_RETURNTYPE,
				false);
		store.setDefault(PreferenceConstants.APPEARANCE_METHOD_TYPEPARAMETERS,
				true);
		store.setDefault(
				PreferenceConstants.APPEARANCE_PKG_NAME_PATTERN_FOR_PKG_VIEW,
				""); //$NON-NLS-1$

		store.setDefault(PreferenceConstants.SHOW_SOURCE_MODULE_CHILDREN, true);

		store.setDefault(
				PreferenceConstants.CODEASSIST_AUTOACTIVATION_TRIGGERS, ".");
	}
}
