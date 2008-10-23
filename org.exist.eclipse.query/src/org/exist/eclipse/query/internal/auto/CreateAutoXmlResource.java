/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import org.exist.eclipse.auto.data.Automation;
import org.exist.eclipse.browse.create.CreateXmlResource;
import org.exist.eclipse.browse.document.IDocumentItem;

/**
 * This class can create an default xml resource document.
 * 
 * @author Markus Tanner
 */
public class CreateAutoXmlResource extends CreateXmlResource {

	@Override
	protected String getContent() {
		return Automation.createEmptyAutomationXml();
	}

	@Override
	protected void checkFileName(IDocumentItem item) {
		super.checkFileName(item);
	}

}
