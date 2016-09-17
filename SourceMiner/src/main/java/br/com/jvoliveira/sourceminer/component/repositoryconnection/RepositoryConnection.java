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

/**
 * @author Joao Victor
 *
 */
public interface RepositoryConnection {

	RepositoryConnector getConnector();
	
	void setConnector(RepositoryConnector connector);
	
	RepositoryParse getParse();
	
	void openConnection();
	
	void closeConnection();
	
	boolean testConnection();
	
	Boolean isConnectionOpened();
	
	Boolean isGit();
	
	Boolean isSVN();
	
	Boolean isCVS();
	
	//TODO: Implementar as demais operações de repositório
	
	List<RepositoryItem> getAllProjectItens(Project project);
	
	List<RepositoryItem> getItensInRevision(Project project, Integer startRevision, Integer endRevision);
	
	List<RepositoryRevisionItem> getRevisionItensInProjectRange(Project project, ProjectConfiguration config);
	
	List<RepositoryRevision> getAllProjectRevision(Project project);
	
	List<RepositoryRevision> getRevisionsInRange(Project project, Integer start, Integer end);
	
	Long getLastRevisionNumber(Project project);
	
	String getFileContent(String path, Long revision);
	
}
