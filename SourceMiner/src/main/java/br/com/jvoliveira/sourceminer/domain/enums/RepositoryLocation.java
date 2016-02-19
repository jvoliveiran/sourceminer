/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.enums;

/**
 * @author Joao Victor
 *
 */
public enum RepositoryLocation {

	LOCAL("local"), REMOTE("remote");
	
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
}
