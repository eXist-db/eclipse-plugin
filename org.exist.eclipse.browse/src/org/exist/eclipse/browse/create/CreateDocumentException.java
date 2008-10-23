package org.exist.eclipse.browse.create;

import org.exist.eclipse.browse.document.IDocumentItem;

/**
 * Exception, if you have a problem to create a new document.
 * 
 * @author Pascal Schmidiger
 */
public class CreateDocumentException extends Exception {

	private static final long serialVersionUID = 4185329531943550369L;

	public CreateDocumentException(IDocumentItem item) {
		super("Create document '" + item.getPath() + "' failed.");
	}

	public CreateDocumentException(IDocumentItem item, Throwable cause) {
		super("Create document '" + item.getPath() + "' failed.", cause);
	}

	public CreateDocumentException(IDocumentItem item, String message) {
		super("Create document '" + item.getPath() + "' failed. " + message);
	}
}
