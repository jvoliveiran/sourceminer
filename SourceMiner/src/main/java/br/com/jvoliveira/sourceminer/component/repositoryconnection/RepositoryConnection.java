/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.exceptions.RepositoryConnectionException;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryConnection {

	RepositoryConnector getConnector();
	
	void setConnector(RepositoryConnector connector);
	
	RepositoryParse getParse();
	
	void openConnection() throws RepositoryConnectionException;
	
	void closeConnection();
	
	boolean testConnection() throws RepositoryConnectionException;
	
	Boolean isConnectionOpened();
	
	Boolean isGit();
	
	Boolean isSVN();
	
	Boolean isCVS();
	
	//TODO: Implementar as demais operações de repositório
	
	List<RepositoryItem> getAllProjectItens(Project project);
	
	List<RepositoryRevisionItem> getRevisionItensInProjectRange(Project project, ProjectConfiguration config);
	
	List<RepositoryRevision> getAllProjectRevision(Project project);
	
	List<RepositoryRevision> getRevisionsInRange(Project project, String start, String end);
	
	String getLastRevisionNumber(Project project);
	
	String getFileContent(String path, String revision);
	
	String getNextRevisionToSync(Project project, String revisionLastSync);
	
}
