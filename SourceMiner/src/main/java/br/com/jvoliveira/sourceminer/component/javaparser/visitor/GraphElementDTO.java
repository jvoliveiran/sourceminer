/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

/**
 * @author João Victor
 *
 */
public class GraphElementDTO {

	private String elementName;
	
	private GraphElementType elementType;
	
	public GraphElementDTO(String name, GraphElementType type){
		this.elementName = name;
		this.elementType = type;
	}

	public GraphElementType getElementType() {
		return elementType;
	}

	public void setElementType(GraphElementType elementType) {
		this.elementType = elementType;
	}
	
	public boolean isMethodCall(){
		return this.elementType.isMethodCall();
	}
	
	public boolean isAccessField(){
		return this.elementType.isAccessField();
	}

	public String getElementName() {
		if(isMethodCall())
			return elementName;
		return elementName.split(">>")[0];
	}
	
	public String getAuxiliarElementName() {
		if(isMethodCall())
			return "";
		try{
			return elementName.split(">>")[1];
		}catch (Exception e) {
			System.err.println("ERROR: Elemento auxiliar incorreto: " + elementName);
			return "";
		}
	}
	
	public String getRawElementName() {
		return this.elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
}
