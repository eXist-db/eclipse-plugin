package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.jface.text.IDocument;

/**
 * Constants for the different partitions.
 * 
 * @author Pascal Schmidiger
 */
public interface IXQueryPartitions {

	public final static String XQUERY_PARTITIONING = "__xquery_partitioning";

	public final static String XQUERY_COMMENT = "__xquery_comment";
	public final static String XQUERY_STRING = "__xquery_string";

	public final static String[] XQUERY_PARITION_TYPES = new String[] {
			IXQueryPartitions.XQUERY_STRING, IXQueryPartitions.XQUERY_COMMENT,
			IDocument.DEFAULT_CONTENT_TYPE };
}
