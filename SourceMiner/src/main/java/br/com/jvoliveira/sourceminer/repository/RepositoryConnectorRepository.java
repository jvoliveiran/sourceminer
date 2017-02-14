/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.User;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryConnectorRepository 
	extends CrudRepository<RepositoryConnector, Long>{

	List<RepositoryConnector> findByUser(User user);
	
}
