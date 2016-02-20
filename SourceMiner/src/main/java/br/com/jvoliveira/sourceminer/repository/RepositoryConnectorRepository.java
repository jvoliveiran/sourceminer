/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.security.domain.User;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryConnectorRepository 
	extends CrudRepository<RepositoryConnector, Long>{

	List<RepositoryConnector> findByUser(User user);
	
}
