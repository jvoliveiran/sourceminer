package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;

/**
 * @author Joao Victor
 *
 */
public interface GenericClassVisitor{

	public abstract void init();
	
	public abstract boolean isReady();
	
	public void execute(CompilationUnit compUnit, Object arg, Map<String,Object> resultMap);
}
