package br.com.jvoliveira.sourceminer.domain.enums;

/**
 * 
 * @author Joao Victor
 *
 */
public enum CommitType {

	ADD("Adição",'A'),
	MOD("Modificação",'M');
	
	private String description;
	private Character initial;
	
	private CommitType(String type, Character initial){
		this.description = type;
		this.initial = initial;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Character getInitial(){
		return this.initial;
	}
	
	public boolean isAdded(){
		return this == ADD;
	}
	
	public boolean isModified(){
		return this == MOD;
	}
}
