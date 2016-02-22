/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionFactory {
	
	//TODO: Implementar factory para demais repositorios
	public RepositoryConnection buildRepositoryConnection(RepositoryConnector connector){
		if(connector.getVersionManager().isSVNManager())
			return new RepositoryConnectionSVN(connector); 
		
		return null;
	}
	
}
