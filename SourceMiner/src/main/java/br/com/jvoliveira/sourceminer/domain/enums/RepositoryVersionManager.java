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
	
	public boolean isGITManager(RepositoryVersionManager manager){
		return manager == GIT;
	}
	
	public boolean isCVSManager(RepositoryVersionManager manager){
		return manager == CVS;
	}
	
	public boolean isSVNManager(RepositoryVersionManager manager){
		return manager == SVN;
	}
	
	public static List<RepositoryVersionManager> getValues(){
		return Arrays.asList(RepositoryVersionManager.values());
	}
	
}
