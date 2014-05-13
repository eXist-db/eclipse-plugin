package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.dltk.ui.text.ScriptOutlineInformationControl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Control, which is used to show the outline correctly.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryOutlineInformationControl extends
		ScriptOutlineInformationControl {

	public XQueryOutlineInformationControl(Shell parent, int shellStyle,
			int treeStyle, String commandId) {
		super(parent, shellStyle, treeStyle, commandId);
	}

	@Override
	protected IPreferenceStore getPreferenceStore() {
		return XQueryUI.getDefault().getPreferenceStore();
	}
}
