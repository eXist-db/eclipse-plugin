/**
 * 
 */
package org.exist.eclipse.xquery.core.internal.parser.visitors;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.expressions.MethodCallExpression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.exist.eclipse.xquery.core.internal.parser.ast.XQueryFieldDeclaration;

/**
 * Build from the "AST Node" model the "Element" model.
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySourceElementRequestVisitor extends
		SourceElementRequestVisitor {

	public XQuerySourceElementRequestVisitor(ISourceElementRequestor requesor) {
		super(requesor);
	}

	@Override
	public boolean endvisit(Statement statement) throws Exception {
		if (statement instanceof XQueryFieldDeclaration) {
			this.fRequestor.exitField(statement.sourceEnd());
		}
		return super.endvisit(statement);
	}

	@Override
	public boolean visit(Expression expression) throws Exception {
		super.visit(expression);

		if (expression instanceof MethodCallExpression) {
			MethodCallExpression cae = (MethodCallExpression) expression;
			String name = cae.getName();
			int nargs = cae.getArgs().getChilds().size();
			this.fRequestor.acceptMethodReference(name, nargs, cae
					.sourceStart(), cae.sourceEnd());
		}
		return true;
	}

	@Override
	public boolean visit(Statement statement) throws Exception {
		super.visit(statement);
		if (statement instanceof XQueryFieldDeclaration) {
			XQueryFieldDeclaration field = XQueryFieldDeclaration.class
					.cast(statement);
			FieldInfo info = new ISourceElementRequestor.FieldInfo();
			info.modifiers = field.getModifiers();
			info.name = field.getName();
			info.nameSourceStart = field.getNameStart();
			info.nameSourceEnd = field.getNameEnd() - 1;
			info.declarationStart = field.sourceStart();

			this.fRequestor.enterField(info);
		}

		return true;
	}

}
