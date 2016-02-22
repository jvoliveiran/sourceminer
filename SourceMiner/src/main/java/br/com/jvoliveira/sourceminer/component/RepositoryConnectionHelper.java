/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

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
	
	public void loadRepositoryInSession(RepositoryConnector connector){
		RepositoryConnection repositoryConnection = factory.buildRepositoryConnection(connector);
		repositoryConnection.openConnection();
		repositoryConnectionSession.setConnection(repositoryConnection);
	}
	
}
