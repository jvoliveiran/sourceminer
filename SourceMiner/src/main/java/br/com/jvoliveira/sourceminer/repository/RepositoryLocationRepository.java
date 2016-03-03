/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryLocationRepository extends CrudRepository<RepositoryLocation, Long>{

	List<RepositoryLocation> findByEnable(Boolean enable);
	
}
