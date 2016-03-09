/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.enums;

/**
 * @author Joao Victor
 *
 */
public enum FeatureType {

	BUGFIX("Correção"), REFACTORING("Refatoração"), NEW_FEATURE("Nova Funcionalidade"),
	MERGE("Merge"), EVOLUTION("Evolução");
	
	private String description;
	
	private FeatureType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isBugfix(){
		return this == BUGFIX;
	}
	
	public boolean isRefactioring(){
		return this == REFACTORING;
	}
	
	public boolean isNewFeature(){
		return this == NEW_FEATURE;
	}
	
	public boolean isMerge(){
		return this == MERGE;
	}
	
	public boolean isEvolution(){
		return this == EVOLUTION;
	}
	
}
