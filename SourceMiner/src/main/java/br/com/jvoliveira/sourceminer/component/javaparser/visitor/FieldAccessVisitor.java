/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
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
		MethodDeclaration methodWhichAccessField = getMethodWhichAccessField(fieldAccess.getParentNode());
		String fieldName = fieldAccess.getFieldExpr().toString();
		if(methodWhichAccessField != null)
			fieldName = fieldName + ">>" + methodWhichAccessField.getName();
		resultMap.put(mapKeyPrefix+fieldName, fieldAccess.getScope().toString());
	}

	private MethodDeclaration getMethodWhichAccessField(Node parentNode) {
		if(parentNode instanceof MethodDeclaration){
			return (MethodDeclaration) parentNode;
		}else if(parentNode.getParentNode() != null)
			return getMethodWhichAccessField(parentNode.getParentNode());
		else
			return null;
	}
	
}
