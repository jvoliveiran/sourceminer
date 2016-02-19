/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.enums;

/**
 * @author Joao Victor
 *
 */
public enum RepositoryVersionManager {

	GIT("git"),
	SVN("svn"),
	CVS("cvs");
	
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
}
