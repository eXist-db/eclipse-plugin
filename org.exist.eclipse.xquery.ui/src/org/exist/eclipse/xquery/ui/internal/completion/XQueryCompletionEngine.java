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
	IScriptProject project;
	private CompletionRequestor requestor;
	private int actualCompletionPosition;
	private int offset;

	public XQueryCompletionEngine() {
	}

	public void complete(IModuleSource module, int position, int pos) {
		this.actualCompletionPosition = position;
		this.offset = pos;

		final String content = module.getSourceContents();
		final String wordStarting = getWordStarting(content, position, 10);

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

	private String getWordStarting(String content, int position, int maxLen) {
		if (position <= 0 || position > content.length()) {
			return "";
		}
		final int original = position;

		while (position > 0
				&& maxLen > 0
				&& XQuerySyntaxUtils.isXQueryIdentifierPart((char) (content
						.charAt(position - 1)))) {
			--position;
			--maxLen;
		}
		return content.substring(position, original);
	}

	private void createProposal(IXQueryMethod method, String prefix) {
		CompletionProposal proposal = createProposal(
				CompletionProposal.METHOD_DECLARATION,
				this.actualCompletionPosition);
		proposal.setFlags(method.getFlags());
		String[] params = null;
		params = method.getParameters();

		if (params != null && params.length > 0) {
			proposal.setParameterNames(params);
		}
		proposal.setName(method.getName());
		proposal.setCompletion(method.getName());
		proposal.setReplaceRange(actualCompletionPosition - offset
				- prefix.length(), actualCompletionPosition - offset
				- prefix.length());
		proposal.setRelevance(20);
		proposal.setModelElement(null);
		this.requestor.accept(proposal);
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
								CompletionProposal.METHOD_DECLARATION,
								this.actualCompletionPosition);
						proposal.setFlags(method.getFlags());

						IParameter[] params = null;
						try {
							params = method.getParameters();
						} catch (ModelException e) {
							// Ignore
						}

						if (params != null && params.length > 0) {
							String[] paramNames = new String[params.length];
							for (int i=0; i<params.length; i++) {
								paramNames[i] = params[i].getName();
							}
							proposal.setParameterNames(paramNames);
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

	@SuppressWarnings("unchecked")
	public void setOptions(Map options) {
	}

	public void setProject(IScriptProject project) {
		this.project = project;
	}

	public void setRequestor(CompletionRequestor requestor) {
		this.requestor = requestor;
	}

	protected CompletionProposal createProposal(int kind, int completionOffset) {
		CompletionProposal proposal = CompletionProposal.create(kind,
				completionOffset - this.offset);

		return proposal;
	}

	protected int getEndOfEmptyToken() {
		return 0;
	}

	protected String processMethodName(IMethod method, String token) {
		return null;
	}

	protected String processTypeName(IType method, String token) {
		return null;
	}
}
