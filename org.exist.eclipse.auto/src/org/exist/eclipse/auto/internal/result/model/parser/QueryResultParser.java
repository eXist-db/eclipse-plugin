/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model.parser;

import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.result.model.QueryResultEntity;
import org.exist.eclipse.auto.internal.result.model.ResultModel;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This class parses the given result xml for query result entities.
 * 
 * @author Markus Tanner
 */
public class QueryResultParser implements AutoTags, ContentHandler {

	private final ResultModel _resultModel;
	private QueryResultEntity _queryResultEntity;
	private ContentHandler _delegate;
	private StringBuilder _value;

	/**
	 * @param resultModel
	 */
	public QueryResultParser(ResultModel resultModel) {
		_resultModel = resultModel;
		_value = new StringBuilder(50);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (_delegate != null) {
			_delegate.characters(ch, start, length);
		} else {
			_value.append(new String(ch, start, length));
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (RUNS.equals(name)) {
			_delegate = null;
		}
		if (_delegate != null) {
			_delegate.endElement(uri, localName, name);
		} else {
			if (NOTE.equals(name)) {
				_queryResultEntity.setNotes(_value.toString().trim());
			} else if (QUANTITY.equals(name)) {
				_queryResultEntity.setQuantity(Integer.parseInt(_value
						.toString().trim()));
			} else if (CONTENT.equals(name)) {
				_queryResultEntity.setQuery(_value.toString().trim());
			} else if (AVGCOMPILATION.equals(name)) {
				_queryResultEntity.setAvgCompTime(Integer.parseInt(_value
						.toString().trim()));
			} else if (AVGEXECUTION.equals(name)) {
				_queryResultEntity.setAvgExecTime(Integer.parseInt(_value
						.toString().trim()));
			}
			_value = new StringBuilder(50);
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		if (_delegate != null) {
			_delegate.processingInstruction(target, data);
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (_delegate != null) {
			_delegate.startElement(uri, localName, name, atts);
		} else {
			if (QUERY.equals(name)) {
				_queryResultEntity = new QueryResultEntity(_resultModel);
				if (atts.getLength() > 0) {
					_queryResultEntity.setName(atts.getValue(0));
				}
			} else if (RUNS.equals(name)) {
				_delegate = new RunParser(_queryResultEntity);
			}
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

}
