/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;

/**
 * @author Joao Victor
 *
 */
public abstract class GenericClassParser {
	
	private GenericClassVisitor classVisitor;

	public abstract List<ItemAsset> parserMethods();
	
	public abstract List<ItemAsset> parserField();
	
	public abstract List<ItemAsset> parserImport();
	
	protected void setGenericClassVisitor(GenericClassVisitor classVisitor){
		this.classVisitor = classVisitor;
	}
	
	protected GenericClassVisitor getGenericClassVisitor(){
		return this.classVisitor;
	}
	
}
