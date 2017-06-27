/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

/**
 * @author João Victor
 *
 */
public enum GraphElementType {

	METHOD_CALL(0, "Method Call"),
	ACCESS_FIELD(1, "Access Field");
	
	private int type;
	private String description;
	
	private GraphElementType(int type, String description){
		this.type = type;
		this.description = description;
	}
	
	public boolean isMethodCall(){
		return this.type == 0;
	}
	
	public boolean isAccessField(){
		return this.type == 1;
	}
	
	@Override
	public String toString(){
		return this.description;
	}
}
