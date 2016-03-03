/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;

/**
 * @author Joao Victor
 *
 */
public interface ProjectRepository extends CrudRepository<Project, Long>{

	List<Project> findByRepositoryLocation(RepositoryLocation location);
	
}
