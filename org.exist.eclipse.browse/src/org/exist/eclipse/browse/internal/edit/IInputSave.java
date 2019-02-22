/**
 * IInputSave.java
 */
package org.exist.eclipse.browse.internal.edit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;

/**
 * This interface defines the methods for saving documents.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IInputSave {
	/**
	 * Save the given <code>document</code>.
	 * 
	 * @param monitor  progress monitor.
	 * @param document contains the content of the document.
	 * @throws CoreException
	 */
	public void setContents(IProgressMonitor monitor, IDocument document) throws CoreException;
}
