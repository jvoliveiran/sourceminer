/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.exceptions.RepositoryConnectionException;

/**
 * @author Joao Victor
 *
 */
@Component
public class RepositoryConnectionHelper {

	private RepositoryConnectionFactory factory;
	
	@Autowired
	private RepositoryConnectionSession repositoryConnectionSession;
	
	public RepositoryConnectionHelper(){
		factory = new RepositoryConnectionFactory();
	}
	
	public RepositoryConnection getRepositoryConnectionByConnector(RepositoryConnector connector){
		return factory.buildRepositoryConnection(connector);
	}
	
	public void loadRepositoryInSession(RepositoryConnector connector) throws RepositoryConnectionException{
		RepositoryConnection repositoryConnection = getRepositoryConnectionByConnector(connector);
		repositoryConnection.openConnection();
		repositoryConnectionSession.setConnection(repositoryConnection);
	}
	
}
