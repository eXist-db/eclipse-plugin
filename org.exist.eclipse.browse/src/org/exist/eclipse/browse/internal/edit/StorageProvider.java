/**
 * StorageProvider.java
 */
package org.exist.eclipse.browse.internal.edit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.StorageDocumentProvider;

/**
 * @author Pascal Schmidiger
 * 
 */
public class StorageProvider extends StorageDocumentProvider {
	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
			throws CoreException {
		if (element instanceof IStorageEditorInput) {
			Object obj = IStorageEditorInput.class.cast(element).getAdapter(IInputSave.class);
			if (obj != null) {
				IInputSave.class.cast(obj).setContents(monitor, document);
			}
		} else {
			super.doSaveDocument(monitor, element, document, overwrite);
		}
	}

	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		return new AnnotationModel();
	}
}
