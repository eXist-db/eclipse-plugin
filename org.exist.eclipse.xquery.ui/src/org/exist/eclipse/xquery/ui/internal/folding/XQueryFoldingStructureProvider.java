package org.exist.eclipse.xquery.ui.internal.folding;

import org.eclipse.core.runtime.ILog;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.exist.eclipse.xquery.core.XQueryNature;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryPartitions;
import org.exist.eclipse.xquery.ui.internal.text.XQueryPartitionScanner;

/**
 * Provider for the folding initialization.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryFoldingStructureProvider extends
		AbstractASTFoldingStructureProvider {

	private boolean _initCollapseComments = true;

	// private IElementCommentResolver _elementCommentResolver = new
	// DefaultElementCommentResolver();

	public String getCommentPartition() {
		return IXQueryPartitions.XQUERY_COMMENT;
	}

	protected ILog getLog() {
		return XQueryUI.getDefault().getLog();
	}

	protected String getPartition() {
		return IXQueryPartitions.XQUERY_PARTITIONING;
	}

	protected IPartitionTokenScanner getPartitionScanner() {
		return new XQueryPartitionScanner();
	}

	protected String[] getPartitionTypes() {
		return IXQueryPartitions.XQUERY_PARITION_TYPES;
	}

	protected String getNatureId() {
		return XQueryNature.NATURE_ID;
	}

	protected void initializePreferences(IPreferenceStore store) {
		super.initializePreferences(store);
		fFoldNewLines = true;
		_initCollapseComments = true;
	}

	protected boolean initiallyCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return false;
	}

	protected boolean initiallyCollapseComments(
			FoldingStructureComputationContext ctx) {
		return ctx.allowCollapsing() && _initCollapseComments;
	}

	protected boolean mayCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return true;
	}

	// public IElementCommentResolver getElementCommentResolver() {
	// return _elementCommentResolver;
	// }
}
