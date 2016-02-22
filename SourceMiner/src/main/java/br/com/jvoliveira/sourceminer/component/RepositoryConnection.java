/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryConnection {

	RepositoryConnector getConnector();
	
	void setConnector(RepositoryConnector connector);
	
	void openConnection();
	
	void closeConnection();
	
	Boolean isConnectionOpened();
	
	Boolean isGit();
	
	Boolean isSVN();
	
	Boolean isCVS();
	
	//TODO: Implementar as demais operações de repositório
	
}
