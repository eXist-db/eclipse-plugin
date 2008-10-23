/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.xerces.parsers.SAXParser;
import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.result.model.parser.ResultParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class is responsible to convert the given automation result data into an
 * auto model.
 * 
 * @author Markus Tanner
 */
public class ResultModelConverter implements AutoTags {

	/**
	 * Gets an IAutoModel for a given xml string.
	 * 
	 * @param resultXml
	 * @return IAutoModel
	 * @throws AutoException
	 */
	public static IResultModel getResultModel(String resultXml)
			throws AutoException {

		InputStream input = null;
		try {
			input = new ByteArrayInputStream(resultXml.getBytes(Charset
					.forName("UTF-8").name()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		InputSource source = new InputSource(input);

		SAXParser parser = new SAXParser();
		ResultParser resultParser = new ResultParser();
		parser.setContentHandler(resultParser);

		try {
			parser.parse(source);
		} catch (SAXException e) {
			throw new AutoException(e);
		} catch (IOException e) {
			throw new AutoException(e);
		}

		return resultParser.getResultModel();
	}

}
