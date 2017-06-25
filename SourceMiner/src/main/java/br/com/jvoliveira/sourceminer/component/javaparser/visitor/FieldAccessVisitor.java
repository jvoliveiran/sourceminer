/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author João Victor
 *
 */
public class FieldAccessVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{

	private boolean ready;
	private Map<String,Object> resultMap;
	private String mapKeyPrefix = "FieldAccessExpr_";
	
	@Override
	public void init() {
		ready = false;
	}
	
	@Override
	public boolean isReady() {
		return ready;
	}
	@Override
	public void execute(CompilationUnit compUnit, Object arg, Map<String, Object> resultMap) {
		this.resultMap = resultMap;
		super.visit(compUnit, arg);
		
		ready = true;
	}
	
	@Override
	public void visit(FieldAccessExpr fieldAccess, Object arg) {
		resultMap.put(mapKeyPrefix+fieldAccess.getFieldExpr().toString(), fieldAccess.getScope().toString());
	}
	
}
