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
public enum RepositoryLocationType {

	LOCAL("Local"), REMOTE("Remoto");
	
	private String location;
	
	private RepositoryLocationType(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public boolean isRemote(){
		return this == REMOTE;
	}
	
	public boolean isLocal(){
		return this == LOCAL;
	}
	
	public static List<RepositoryLocationType> getValues(){
		return Arrays.asList(RepositoryLocationType.values());
	}
	
}
