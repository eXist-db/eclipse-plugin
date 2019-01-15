package org.exist.eclipse.query.internal.xquery.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.query.internal.xquery.run.RunQuery;
import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

/**
 * Job which gets the functions from eXist. This is usefull for code assistant.
 * 
 * @author Pascal Schmidiger
 */
public class GetFunctionJob extends Job {
	private final IBrowseItem _item;
	private Collection<IXQueryMethod> _methods;
	private boolean _fetched;

	public GetFunctionJob(IBrowseItem item) {
		super("Get functions from " + item.getConnection());
		_item = item;
		_methods = new ArrayList<>();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		_methods = fillMethods();
		_fetched = true;
		return Status.OK_STATUS;
	}

	/**
	 * @return <code>true</code>, if the function was fetched from eXist,
	 *         elsewhere <code>false</code>.
	 */
	public boolean hasFetched() {
		return _fetched;
	}

	/**
	 * Return the methods, which are fetched from eXist, by the given
	 * <code>prefix</code>.
	 * 
	 * @param prefix
	 *            start of the method; could be <code>null</code>.
	 * @return the methods which matched with the given <code>prefix</code>.
	 */
	public IXQueryMethod[] getMethods(String prefix) {
		if (prefix != null && prefix.length() > 0) {
			Collection<IXQueryMethod> result = new ArrayList<>();
			for (IXQueryMethod method : _methods) {
				if (method.getName().startsWith(prefix)) {
					result.add(method);
				}
			}
			return result.toArray(new IXQueryMethod[result.size()]);

		} else {
			return _methods.toArray(new IXQueryMethod[_methods.size()]);
		}
	}

	private Collection<IXQueryMethod> fillMethods() {
		Collection<String> fromExist = getMethodsFromExist();
		Collection<IXQueryMethod> methods = new ArrayList<>(
				fromExist.size());
		for (String methodString : fromExist) {
			String name = null;
			name = methodString.substring(0, methodString.indexOf("("));
			ExistXQueryMethod method = new ExistXQueryMethod(name.trim());
			String paramString = methodString.substring(methodString
					.indexOf("("), methodString.indexOf(")"));
			if (paramString.length() > 1) {
				StringTokenizer token = new StringTokenizer(paramString, ",");
				while (token.hasMoreTokens()) {
					String param = token.nextToken();
					param = param.substring(param.lastIndexOf(" "));
					method.addParameter(param.trim(), "");
				}
			}
			methods.add(method);
		}
		return methods;
	}

	private Collection<String> getMethodsFromExist() {
		Collection<String> methods = new ArrayList<>();
		ResourceSet result = null;
		try {
			RunQuery existQuery = new RunQuery(_item, getQuery());
			existQuery.init();

			CompiledExpression compiled = existQuery.compile();
			result = existQuery.execute(compiled);

			ResourceIterator i = result.getIterator();

			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				methods.add((String) r.getContent());
			}
		} catch (Exception e) {
			// do nothing
		} finally {
			if (result != null) {
				try {
					result.clear();
				} catch (XMLDBException e) {
					// ignore
				}
			}
		}
		return methods;
	}

	private String getQuery() {
		StringBuilder query = new StringBuilder();
		query.append("declare function local:getFunctions(){\n");
		query.append("let $functions := util:registered-functions()\n");
		query.append("for $function in $functions\n");
		query
				.append("return util:describe-function($function)//signature/string()\n");
		query.append("};\n");
		query.append("local:getFunctions()");
		return query.toString();
	}
}
