package org.exist.eclipse.query.internal.xquery.run;

import javax.xml.transform.OutputKeys;

import org.exist.eclipse.browse.browse.IBrowseItem;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XQueryService;

/**
 * A wrapper class for running queries on eXist.
 * 
 * @author Pascal Schmidiger
 */
public class RunQuery {
	private final String _content;
	private final IBrowseItem _item;
	private XQueryService _service;

	public RunQuery(IBrowseItem item, String content) {
		_item = item;
		_content = content;
	}

	public void init() throws Exception {
		_service = (XQueryService) _item.getCollection().getService("XQueryService", "1.0");
		_service.setProperty(OutputKeys.INDENT, "yes");
	}

	public CompiledExpression compile() throws Exception {
		return _service.compile(_content);
	}

	public ResourceSet execute(CompiledExpression compiled) throws Exception {
		return _service.execute(compiled);
	}
}
