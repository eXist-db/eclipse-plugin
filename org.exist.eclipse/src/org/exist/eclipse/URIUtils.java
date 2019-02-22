/**
 * URIUtils
 */

package org.exist.eclipse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utilities for URI related functions
 *
 * @author Patrick Reinhart
 */
public final class URIUtils {
	private URIUtils() {
	}

	/**
	 * This method decodes the provided uri for human readability. The method simply
	 * wraps URLDecoder.decode(uri,"UTF-8). It is places here to provide a friendly
	 * way to decode URIs encoded by urlEncodeUtf8()
	 * 
	 * @param uri The uri to decode
	 * @return The decoded value of the supplied uri
	 */
	public static String urlDecodeUtf8(String uri) {
		try {
			return URLDecoder.decode(uri, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			// wrap with a runtime Exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is a wrapper for
	 * {@link java.net.URLEncoder#encode(java.lang.String,java.lang.String)} It
	 * calls this method, suppying the url parameter as the first parameter, and
	 * "UTF-8" (the W3C recommended encoding) as the second.
	 * UnsupportedEncodingExceptions are wrapped in a runtime exception.
	 * 
	 * IMPORTANT: the java.net.URLEncoder class encodes a space (" ") as a "+". The
	 * proper method of encoding spaces in the path of a URI is with "%20", so this
	 * method will replace all instances of "+" in the encoded string with "%20"
	 * before returning. This means that XmldbURIs constructed from
	 * java.net.URLEncoder#encoded strings will not be String equivalents of
	 * XmldbURIs created with the result of calls to this function.
	 * 
	 * @param uri The uri to encode
	 * @return The UTF-8 encoded value of the supplied uri
	 */
	public static String urlEncodeUtf8(String uri) {
		try {
			final String almostEncoded = URLEncoder.encode(uri, "UTF-8");
			return almostEncoded.replaceAll("\\+", "%20");
		} catch (final UnsupportedEncodingException e) {
			// wrap with a runtime Exception
			throw new RuntimeException(e);
		}
	}
}
