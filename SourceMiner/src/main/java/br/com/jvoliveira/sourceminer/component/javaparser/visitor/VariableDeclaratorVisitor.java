/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.com.jvoliveira.sourceminer.component.javaparser.JavaParserUtils;

/**
 * @author João Victor
 *
 */
public class VariableDeclaratorVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{
	
	private boolean ready;
	private Map<String,Object> resultMap;
	
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
		this.resultMap = resultMap;
		super.visit(compUnit, arg);
		ready = true;
	}
	
	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		List<VariableDeclarator> myVars = n.getVars();
		for (VariableDeclarator vars : myVars) {
			if(JavaParserUtils.isCustomClassType(n.getType().toString()))
				this.resultMap.put("VariableDeclaration_"+vars.getId().getName(), n.getType().toString());
		}
	}
	
}
