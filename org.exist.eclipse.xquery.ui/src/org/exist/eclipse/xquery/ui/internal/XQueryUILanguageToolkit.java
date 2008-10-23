package org.exist.eclipse.xquery.ui.internal;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.ui.plugin.AbstractUIPlugin;
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
		public void getElementLabel(IModelElement element, long flags,
				StringBuffer buf) {
			StringBuffer buffer = new StringBuffer(60);
			super.getElementLabel(element, flags, buffer);
			String s = buffer.toString();
			if (s != null && !s.startsWith(element.getElementName())) {
				if (s.indexOf('$') != -1) {
					s = s.replaceAll("\\$", ".");
				}
			}
			buf.append(s);
		}

		protected char getTypeDelimiter() {
			return '$';
		}
	};

	public ScriptElementLabels getScriptElementLabels() {
		return new XQueryScriptElementLabels();
	}

	protected AbstractUIPlugin getUIPLugin() {
		return XQueryCorePlugin.getDefault();
	}

	public IDLTKLanguageToolkit getCoreToolkit() {
		return XQueryLanguageToolkit.getDefault();
	}

	@Override
	public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new XQuerySourceViewerConfiguration(getTextTools()
				.getColorManager(), getPreferenceStore(), null,
				getPartitioningId());
	}
}
