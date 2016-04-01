package br.com.jvoliveira.sourceminer.domain.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Joao Victor
 *
 */
public enum AssetType {

	FIELD("Atributo"), METHOD("Metodo"), IMPORT("Import");
	
	private String description;
	
	private AssetType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isField(){
		return this.equals(FIELD);
	}
	
	public boolean isMethod(){
		return this.equals(METHOD);
	}
	
	public boolean isImport(){
		return this.equals(IMPORT);
	}
	
	public static List<AssetType> getValues(){
		return Arrays.asList(AssetType.values());
	}
	
}
