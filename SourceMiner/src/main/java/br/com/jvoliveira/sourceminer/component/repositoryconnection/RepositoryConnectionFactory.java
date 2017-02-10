/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionFactory {
	
	//TODO: Implementar factory para demais repositorios
	public RepositoryConnection buildRepositoryConnection(RepositoryConnector connector){
		if(connector.getRepositoryLocation().getVersionManager().isSVNManager())
			return new RepositoryConnectionSVN(connector);
		else if(connector.getRepositoryLocation().getVersionManager().isGITManager())
			return new RepositoryConnectionGIT(connector);
		
		return null;
	}
	
}
