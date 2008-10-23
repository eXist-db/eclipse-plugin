/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.completion;

import org.exist.eclipse.xquery.ui.completion.ICompletionExtension;
import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;

/**
 * {@link ICompletionExtension} for eXist.
 * 
 * @author Pascal Schmidiger
 */
public class CompletionExtension implements ICompletionExtension {

	private final GetFunctionJob _functionJob;

	public CompletionExtension(GetFunctionJob functionJob) {
		_functionJob = functionJob;
	}

	public IXQueryMethod[] getMethods(String prefix) {
		if (_functionJob.hasFetched()) {
			return _functionJob.getMethods(prefix);
		}
		return new IXQueryMethod[] {};
	}

}
