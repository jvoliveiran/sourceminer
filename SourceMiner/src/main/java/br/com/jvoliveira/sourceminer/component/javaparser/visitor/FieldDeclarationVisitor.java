/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.com.jvoliveira.sourceminer.component.javaparser.JavaParserUtils;

/**
 * @author João Victor
 *
 */
public class FieldDeclarationVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{

	private boolean ready;
	private Map<String,Object> resultMap;
	
	public FieldDeclarationVisitor() {
		// TODO Auto-generated constructor stub
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
		this.resultMap = resultMap;
		super.visit(compUnit, arg);
		
		ready = true;
	}
	
	public void visit(FieldDeclaration fieldDeclaration, Object arg) {
		for (VariableDeclarator variableDeclaration : fieldDeclaration.getVariables()) {
			if(JavaParserUtils.isCustomClassType(fieldDeclaration.getType().toString())){
				this.resultMap.put("FieldDeclaration_"+variableDeclaration.getId().getName(), fieldDeclaration.getType().toString());
				System.out.println("Variable Name: " + variableDeclaration.getId().getName() + " | type: " + fieldDeclaration.getType());
			}
		}
	}
}
