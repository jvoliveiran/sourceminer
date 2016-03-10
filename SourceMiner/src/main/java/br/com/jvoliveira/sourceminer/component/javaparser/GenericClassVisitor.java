package br.com.jvoliveira.sourceminer.component.javaparser;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


/**
 * @author Joao Victor
 *
 */
public abstract class GenericClassVisitor extends VoidVisitorAdapter<Object>{

	public abstract void init();
	
	public abstract boolean isReady();	
}
