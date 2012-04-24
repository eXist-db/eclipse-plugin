/**
 * 
 */
package org.exist.eclipse.auto.data;

import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class can return a basic automation xml file.
 * 
 * @author Markus Tanner
 */
public class Automation implements AutoTags {

	/**
	 * Creates a basic automation xml.
	 * 
	 * @return Automation xml string
	 */
	public static String createEmptyAutomationXml() {
		Element automation = new Element(AUTOMATION);
		Element threadCount = new Element(THREADCOUNT);
		threadCount.setText(Integer.toString(1));
		Element queryOrderType = new Element(QUERYORDERTYPE);
		queryOrderType.setText(QueryOrderType.SEQUENTIAL.toString());
		Element autoNote = new Element(AUTONOTE);
		autoNote.setText("Note");
		Element queries = new Element(QUERIES);

		Element query = new Element(QUERY);
		Element quantity = new Element(QUANTITY);
		quantity.setText(Integer.toString(1));
		Element content = new Element(CONTENT);
		content.setText("//query");

		query.setAttribute(NAME, "Automation - Query");
		query.addContent(quantity);
		query.addContent(content);

		queries.addContent(query);
		automation.addContent(threadCount);
		automation.addContent(queryOrderType);
		automation.addContent(autoNote);
		automation.addContent(queries);

		Document document = new Document(automation);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());

		return outputter.outputString(document);
	}
}
