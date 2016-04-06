/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author Joao Victor
 *
 */
public enum ChangeFileType {

	ADDED("Adicionado"), DELETED("Removido"), UPDATED("Atualizado");
	
	private String description;
	
	private ChangeFileType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isAdd(){
		return this == ADDED;
	}
	
	public boolean isDelete(){
		return this == DELETED;
	}
	
	public boolean isUpdate(){
		return this == UPDATED;
	}

	public static List<ChangeFileType> getValues() {
		return Arrays.asList(ChangeFileType.values());
	}
}
