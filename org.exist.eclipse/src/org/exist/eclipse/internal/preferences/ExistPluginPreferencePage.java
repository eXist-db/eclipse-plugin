package org.exist.eclipse.internal.preferences;

import java.nio.charset.Charset;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class ExistPluginPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Subclass of StringFieldEditor, which checks whether the input is valid.
	 */
	public class UserSuffixFieldEditor extends StringFieldEditor {
		public UserSuffixFieldEditor(String name, String labelText,
				Composite parent) {
			super(name, labelText, 15, parent);
		}

		/**
		 * Verifies the entered string.
		 * 
		 * @return - <code>true</code> if valid
		 */
		@Override
		protected boolean doCheckState() {
			String txt = getTextControl().getText();
			for (int i = 0, n = txt.length(); i < n; i++) {
				if (!Character.isLetterOrDigit(txt.charAt(i))) {
					setErrorMessage("Input not ok!");
					return false;
				}
			}
			return super.doCheckState();
		}
	}

	// the workbench instance
	private IWorkbench workbench;

	/* Constructor */
	public ExistPluginPreferencePage() {
		super(GRID);
		setDescription("General eXist Preferences:\n\n");
	}

	/**
	 * Initialization
	 */
	@Override
	public void init(IWorkbench initialWorkbench) {
		this.workbench = initialWorkbench;
	}

	/**
	 * construct page content
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		workbench.getHelpSystem().setHelp(getControl(),
				getPreferenceHelpContextID());
	}

	/**
	 * Returns the context-id for this PreferencePage
	 * 
	 * @return String - ID for context-sensitive help
	 */
	protected String getPreferenceHelpContextID() {
		return "org.exist.eclipse.preferences_context";
	}

	@Override
	protected void createFieldEditors() {
		Composite composite = getFieldEditorParent();

		// encoding
		String[][] encodings = new String[Charset.availableCharsets().size()][2];
		int counter = 0;
		for (String charsetName : Charset.availableCharsets().keySet()) {
			String[] encoding = encodings[counter];
			encoding[0] = charsetName;
			encoding[1] = charsetName;
			counter++;
		}
		ComboFieldEditor comboFieldEditor = new ComboFieldEditor(
				IExistPreferenceConstants.PREFS_ENCODING,
				IExistPreferenceConstants.PREFS_ENCODING_LABEL, encodings,
				composite);
		addField(comboFieldEditor);
		comboFieldEditor.loadDefault();

	}

}
