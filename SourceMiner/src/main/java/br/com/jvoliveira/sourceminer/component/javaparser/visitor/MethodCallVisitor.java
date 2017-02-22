/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author João Victor
 *
 */
public class MethodCallVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{

	private boolean ready;
	private List<MethodCallExpr> methodCall;

	public MethodCallVisitor() {
		this.methodCall = new ArrayList<MethodCallExpr>();
	}
	
	@Override
	public void init() {
		ready = false;
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	@Override
	public void execute(CompilationUnit compUnit, Object arg, Map<String,Object> resultMap) {
		super.visit(compUnit, arg);
		
		ready = true;
	}

	@Override
	public void visit(MethodCallExpr methodCall, Object arg) {
		this.methodCall.add(methodCall);
		List<Expression> args = methodCall.getArgs();
		if (args != null)
			handleExpressions(args);
	}

	private void handleExpressions(List<Expression> expressions) {
		for (Expression expr : expressions) {
			if (expr instanceof MethodCallExpr)
				visit((MethodCallExpr) expr, null);
			else if (expr instanceof BinaryExpr) {
				BinaryExpr binExpr = (BinaryExpr) expr;
				handleExpressions(Arrays.asList(binExpr.getLeft(), binExpr.getRight()));
			}
		}
	}
	
	public List<MethodCallExpr> getMethodCall(){
		return this.methodCall;
	}
}
