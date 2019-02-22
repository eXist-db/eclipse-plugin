/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model.parser;

import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.exist.eclipse.auto.internal.result.model.IResultModel;
import org.exist.eclipse.auto.internal.result.model.ResultModel;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * The ResultParser is the main parser of the automation result parsing. It
 * fetches the main elements and delegates the other elements.
 * 
 * @author Markus Tanner
 */
public class ResultParser implements AutoTags, ContentHandler {
	private ContentHandler _delegate;

	private ResultModel _resultModel;
	private StringBuilder _value;

	public ResultParser() {
		_value = new StringBuilder(50);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
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
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (QUERIES.equals(name)) {
			_delegate = null;
		}
		if (_delegate != null) {
			_delegate.endElement(uri, localName, name);
		} else {
			if (THREADCOUNT.equals(name)) {
				if (_resultModel != null) {
					_resultModel.setThreadCount(Integer.parseInt(_value.toString().trim()));
				}
			} else if (QUERYCOUNT.equals(name)) {
				if (_resultModel != null) {
					_resultModel.setQueryCount(Integer.parseInt(_value.toString().trim()));
				}
			} else if (QUERYORDERTYPE.equals(name)) {
				if (_resultModel != null) {
					_resultModel.setQueryOrderType(QueryOrderType.valueOf(_value.toString().trim()));
				}
			} else if (AUTONOTE.equals(name)) {
				if (_resultModel != null) {
					_resultModel.setAutoNote(_value.toString().trim());
				}
			}
			_value = new StringBuilder(50);
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
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
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if (_delegate != null) {
			_delegate.startElement(uri, localName, name, atts);
		} else {
			if (AUTOMATIONRESULT.equals(name)) {
				_resultModel = new ResultModel();
			} else if (QUERIES.equals(name)) {
				_delegate = new QueryResultParser(_resultModel);
			}
		}

	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	/**
	 * Returns the IResultModel
	 * 
	 * @return resultmodel
	 */
	public IResultModel getResultModel() {
		return _resultModel;
	}

}
