/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;

/**
 * @author Joao Victor
 *
 */
public interface ProjectRepositoryCustom {

	List<Project> findByRepositoryLocation(RepositoryLocation location);
	
}
