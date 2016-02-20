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
public enum RepositoryLocation {

	LOCAL("Local"), REMOTE("Remoto");
	
	private String location;
	
	private RepositoryLocation(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public boolean isRemote(RepositoryLocation location){
		return location == REMOTE;
	}
	
	public boolean isLocal(RepositoryLocation location){
		return location == LOCAL;
	}
	
	public static List<RepositoryLocation> getValues(){
		return Arrays.asList(RepositoryLocation.values());
	}
	
}
