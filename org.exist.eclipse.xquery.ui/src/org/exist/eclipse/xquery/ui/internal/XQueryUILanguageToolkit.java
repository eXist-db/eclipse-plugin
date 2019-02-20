package org.exist.eclipse.xquery.ui.internal;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.exist.eclipse.xquery.core.XQueryCorePlugin;
import org.exist.eclipse.xquery.core.XQueryLanguageToolkit;
import org.exist.eclipse.xquery.ui.internal.text.XQuerySourceViewerConfiguration;

/**
 * Here you find the skeletal structure about the xquery ui language.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryUILanguageToolkit extends AbstractDLTKUILanguageToolkit {
	private static class XQueryScriptElementLabels extends ScriptElementLabels {
		@Override
		public void getElementLabel(IModelElement element, long flags, StringBuffer buf) {
			buf.append(element.getElementName());
		}
	}

	@Override
	public ScriptElementLabels getScriptElementLabels() {
		return new XQueryScriptElementLabels();
	}

	@Override
	public IDLTKLanguageToolkit getCoreToolkit() {
		return XQueryLanguageToolkit.getDefault();
	}

	@Override
	public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new XQuerySourceViewerConfiguration(getTextTools().getColorManager(), getPreferenceStore(), null,
				getPartitioningId());
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return XQueryCorePlugin.getDefault().getPreferenceStore();
	}
}
