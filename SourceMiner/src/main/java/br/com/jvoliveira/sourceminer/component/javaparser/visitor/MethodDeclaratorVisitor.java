/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.com.jvoliveira.sourceminer.component.javaparser.JavaParserUtils;

/**
 * @author João Victor
 *
 */
public class MethodDeclaratorVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{

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
	public void execute(CompilationUnit compUnit, Object arg, Map<String, Object> resultMap) {
		this.resultMap = resultMap;
		super.visit(compUnit, arg);
		ready = true;
	}
	
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		if(JavaParserUtils.isCustomClassType(n.getType().toString()))
			this.resultMap.put("ClassMethodDeclaration_"+n.getName(), n.getType().toString());
	}

}
