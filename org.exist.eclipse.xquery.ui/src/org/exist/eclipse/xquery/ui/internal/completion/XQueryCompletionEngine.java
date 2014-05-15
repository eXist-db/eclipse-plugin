package org.exist.eclipse.xquery.ui.internal.completion;

import java.util.List;
import java.util.Map;

import org.eclipse.dltk.codeassist.ScriptCompletionEngine;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.IParameter;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.completion.ICompletionExtension;
import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;
import org.exist.eclipse.xquery.ui.internal.text.KeyWordContainer;
import org.exist.eclipse.xquery.ui.internal.text.XQuerySyntaxUtils;

/**
 * The engine of code assist. It will create proposals for keywords, static
 * functions and functions from the document.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCompletionEngine extends ScriptCompletionEngine {

	private IScriptProject project;
	private CompletionRequestor requestor;
	private int actualCompletionPosition;
	private int offset;

	public XQueryCompletionEngine() {
	}

	@Override
	public void complete(IModuleSource module, int position, int pos) {
		this.actualCompletionPosition = position;
		this.offset = pos;

		final String content = module.getSourceContents();
		final String wordStarting = getWordStarting(content, position);

		if (wordStarting.length() != 0) {
			setSourceRange(position - wordStarting.length(), position);
			KeyWordContainer wordContainer = XQueryUI.getDefault()
					.getKeyWordContainer();
			List<String> keywords = wordContainer.getKeyWords();
			for (String keyword : keywords) {
				createProposal(keyword, null, wordStarting);

			}
		}

		// completion for model elements.
		try {
			module.getModelElement().accept(new IModelElementVisitor() {
				@Override
				public boolean visit(IModelElement element) {
					if (element.getElementType() > IModelElement.SOURCE_MODULE) {
						createProposal(element.getElementName(), element,
								wordStarting);
					}
					return true;
				}
			});
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}

		// completion for defined function elements
		List<IXQueryMethod> methods = XQueryMixinModel.getInstance()
				.getMethods(wordStarting);
		for (IXQueryMethod method : methods) {
			createProposal(method, wordStarting);
		}

		// completion for context extensions
		IXQueryEditor editor = XQueryUI.getDefault().getActiveXQueryEditor();
		if (editor != null) {
			IConnectionContext context = editor.getConnectionContext();
			if (context != null) {
				ICompletionExtension extension = context
						.getCompletionExtension();
				if (extension != null) {
					for (IXQueryMethod method : extension
							.getMethods(wordStarting)) {
						createProposal(method, wordStarting);
					}
				}
			}
		}
	}

	private String getWordStarting(String content, int pos) {
		if (pos <= 0 || pos > content.length()) {
			return "";
		}
		final int original = pos;

		while (true) {
			char c = content.charAt(pos - 1);
			if (pos == 0 || !XQuerySyntaxUtils.isXQueryIdentifierPart(c)) {
				break;
			}

			pos--;
		}
		return content.substring(pos, original);
	}

	private void createProposal(IXQueryMethod method, String prefix) {

		String methodName = method.getName();

		boolean hasPrefix = methodName.startsWith(prefix);
		if (hasPrefix) {
			CompletionProposal proposal = createProposal(
					CompletionProposal.METHOD_REF,
					this.actualCompletionPosition);
			proposal.setFlags(method.getFlags());
			String[] params = method.getParameterNames();
			if (params != null && params.length > 0) {
				proposal.setParameterNames(params);
			}

			proposal.setExtraInfo(new MethodCompletionExtraInfo(method
					.getParameterTypes()));

			proposal.setName(methodName);
			proposal.setCompletion(methodName);
			int range = actualCompletionPosition - offset - prefix.length();
			proposal.setReplaceRange(range, range);
			proposal.setRelevance(20);
			proposal.setModelElement(null);
			this.requestor.accept(proposal);
		}
	}

	private void createProposal(String name, IModelElement element,
			String prefix) {
		if (name.startsWith(prefix)) {
			CompletionProposal proposal = null;
			try {
				if (element == null) {
					proposal = this.createProposal(CompletionProposal.KEYWORD,
							this.actualCompletionPosition);
				} else {
					switch (element.getElementType()) {
					case IModelElement.METHOD:
						IMethod method = (IMethod) element;
						proposal = this.createProposal(
								CompletionProposal.METHOD_REF,
								this.actualCompletionPosition);
						proposal.setFlags(method.getFlags());

						try {
							IParameter[] params = method.getParameters();
							if (params != null && params.length > 0) {
								String[] paramNames = new String[params.length];
								for (int i = 0; i < params.length; i++) {
									paramNames[i] = params[i].getName();
								}
								proposal.setParameterNames(paramNames);
							}
						} catch (ModelException e) {
							// Ignore
						}

						break;
					case IModelElement.FIELD:
						proposal = this.createProposal(
								CompletionProposal.FIELD_REF,
								this.actualCompletionPosition);
						proposal.setFlags(((IField) element).getFlags());
						break;
					case IModelElement.TYPE:
						proposal = this.createProposal(
								CompletionProposal.TYPE_REF,
								this.actualCompletionPosition);
						proposal.setFlags(((IType) element).getFlags());
						break;
					default:
						proposal = this.createProposal(
								CompletionProposal.KEYWORD,
								this.actualCompletionPosition);
						break;
					}
				}
				proposal.setName(name);
				proposal.setCompletion(name);
				proposal.setReplaceRange(actualCompletionPosition - offset
						- prefix.length(), actualCompletionPosition - offset
						- prefix.length());
				proposal.setRelevance(20);
				proposal.setModelElement(element);
				this.requestor.accept(proposal);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void setOptions(Map options) {
	}

	@Override
	public void setProject(IScriptProject project) {
		this.project = project;
	}

	@Override
	public void setRequestor(CompletionRequestor requestor) {
		this.requestor = requestor;
	}

	@Override
	protected CompletionProposal createProposal(int kind, int completionOffset) {
		CompletionProposal proposal = CompletionProposal.create(kind,
				completionOffset - this.offset);

		return proposal;
	}

	@Override
	protected int getEndOfEmptyToken() {
		return 0;
	}

	@Override
	protected String processMethodName(IMethod method, String token) {
		return null;
	}

	@Override
	protected String processTypeName(IType method, String token) {
		return null;
	}
}
