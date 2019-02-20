/**
 * 
 */
package org.exist.eclipse.auto.internal.model;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class offers utilities in order to convert the IAutoModel into an
 * Xml-representation and vice versa.
 * 
 * @author Markus Tanner
 */
public class AutoModelConverter implements AutoTags {

	/**
	 * Based on the IAutoModel as input, the xml representation of the model is put
	 * together in this method.
	 * 
	 * @param autoModel
	 * @return The xml representation of the auto model
	 */
	public static String getXml(IAutoModel autoModel) {

		Element automation = new Element(AUTOMATION);
		Element threadCount = new Element(THREADCOUNT);
		Element queryOrderType = new Element(QUERYORDERTYPE);
		Element autoNote = new Element(AUTONOTE);
		Element queries = new Element(QUERIES);

		// add data from model
		threadCount.setText(Integer.toString(autoModel.getThreadCount()));
		queryOrderType.setText(autoModel.getQueryOrderType().toString());
		autoNote.setText(autoModel.getAutoNote());

		for (QueryEntity queryEntity : autoModel.getQueries()) {
			Element query = new Element(QUERY);
			Element note = new Element(NOTE);
			note.setText(queryEntity.getNotes());
			Element quantity = new Element(QUANTITY);
			quantity.setText(Integer.toString(queryEntity.getQuantity()));
			Element content = new Element(CONTENT);
			content.setText(queryEntity.getQuery());

			query.setAttribute(NAME, queryEntity.getName());
			query.addContent(note);
			query.addContent(quantity);
			query.addContent(content);

			queries.addContent(query);
		}

		automation.addContent(threadCount);
		automation.addContent(queryOrderType);
		automation.addContent(autoNote);
		automation.addContent(queries);

		Document document = new Document(automation);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());

		return outputter.outputString(document);
	}

	/**
	 * Based on a string the an IAutoModel object is put together.
	 * 
	 * @param autoModelXml
	 * @return The auto model for a specific xml representation
	 * @throws AutoException
	 */
	public static IAutoModel getAutoModel(String autoModelXml) throws AutoException {

		AutoModel model = new AutoModel();
		Document doc = getValidatedDocument(autoModelXml);

		Element automation = doc.getRootElement();
		Element threadCount = automation.getChild(THREADCOUNT);
		model.setThreadCount(Integer.parseInt(threadCount.getText()));
		Element queryOrderType = automation.getChild(QUERYORDERTYPE);
		if (queryOrderType != null && !queryOrderType.getText().isEmpty()) {
			model.setQueryOrderType(QueryOrderType.valueOf(queryOrderType.getText().toUpperCase()));
		} else {
			model.setQueryOrderType(QueryOrderType.SEQUENTIAL);
		}
		Element autoNote = automation.getChild(AUTONOTE);
		model.setAutoNote(autoNote.getText());
		Element queries = automation.getChild(QUERIES);
		Iterator<?> it = queries.getChildren(QUERY).iterator();
		while (it.hasNext()) {
			Element query = (Element) it.next();
			Element note = query.getChild(NOTE);
			Element quantity = query.getChild(QUANTITY);
			Element content = query.getChild(CONTENT);

			QueryEntity queryEntity = new QueryEntity(query.getAttributeValue(NAME), note != null ? note.getText() : "",
					Integer.parseInt(quantity.getText()), content.getText());

			model.addQuery(queryEntity);
		}
		model.UpdateModel();

		return model;
	}

	// --------------------------------------------------------------------------
	// Private Methods
	// --------------------------------------------------------------------------

	/**
	 * This method converts a String into an Xml-Document. The document is verified
	 * for its validity.
	 * 
	 * @param autoModelXml
	 * @return
	 * @throws AutoException
	 */
	private static Document getValidatedDocument(String autoModelXml) throws AutoException {

		Document doc = new Document();

		SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
		StringReader stringReader = new StringReader(autoModelXml);

		URL url = FileLocator.find(AutoUI.getDefault().getBundle(), new Path("resources/automation.xsd"), null);
		try {
			saxBuilder.setFeature("http://apache.org/xml/features/validation/schema", true);
			saxBuilder.setProperty("http://apache.org/xml/properties/schema" + "/external-noNamespaceSchemaLocation",
					FileLocator.toFileURL(url).toString());
		} catch (IOException e) {
			throw new AutoException("Problem while loading schema: " + e.getMessage(), e);
		}

		// run schema validation while building the document
		try {
			doc = saxBuilder.build(stringReader);
		} catch (JDOMException e) {
			throw new AutoException("Invalid Automation Configuration File: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AutoException("Problem while Loading Automation Configuration File: " + e.getMessage(), e);
		}

		return doc;
	}

}
