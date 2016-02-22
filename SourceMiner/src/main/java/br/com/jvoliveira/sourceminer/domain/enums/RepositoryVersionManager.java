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
public enum RepositoryVersionManager {

	GIT("GIT"),
	SVN("SVN"),
	CVS("CVS");
	
	private String versionManager;
	
	private RepositoryVersionManager(String versionManager){
		this.versionManager = versionManager;
	}
	
	public String getVersionManager(){
		return this.versionManager;
	}
	
	public boolean isGITManager(){
		return this == GIT;
	}
	
	public boolean isCVSManager(){
		return this == CVS;
	}
	
	public boolean isSVNManager(){
		return this == SVN;
	}
	
	public static List<RepositoryVersionManager> getValues(){
		return Arrays.asList(RepositoryVersionManager.values());
	}
	
}
