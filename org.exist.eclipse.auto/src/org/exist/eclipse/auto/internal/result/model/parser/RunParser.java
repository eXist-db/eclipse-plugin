/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model.parser;

import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.result.model.QueryResultEntity;
import org.exist.eclipse.auto.internal.result.model.RunEntity;
import org.exist.eclipse.auto.query.State;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This class parses the given result xml for query run result entities.
 * 
 * @author Markus Tanner
 */
public class RunParser implements AutoTags, ContentHandler {

	private int _index = 0;
	private StringBuilder _value;
	private RunEntity _runEntity;
	private final QueryResultEntity _queryResultEntity;

	public RunParser(QueryResultEntity queryResultEntity) {
		_queryResultEntity = queryResultEntity;
		_value = new StringBuilder(50);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		_value.append(new String(ch, start, length));
	}

	public void endDocument() throws SAXException {

	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (RUN.equals(name)) {
			_queryResultEntity.add(_runEntity);
		}
		if (STATE.equals(name)) {
			_runEntity.setState(State.valueOf(_value.toString().trim()));
		} else if (COMPILATION.equals(name)) {
			_runEntity.setCompilation(Integer
					.parseInt(_value.toString().trim()));
		} else if (EXECUTION.equals(name)) {
			_runEntity.setExecution(Integer.parseInt(_value.toString().trim()));
		}
		_value = new StringBuilder(50);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (RUN.equals(name)) {
			_runEntity = new RunEntity();
			_runEntity.setIndex(++_index);
		}
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

}
