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
public enum MetricType {

	COMPLEXITY("Complexidade"), CHURN("Churn");
	
	private String description;
	
	private MetricType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isChurn(){
		return this == CHURN;
	}
	
	public boolean isComplexity(){
		return this == COMPLEXITY;
	}
	
	public static List<MetricType> getValues(){
		return Arrays.asList(MetricType.values());
	}
}
