/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.repository.custom.ProjectRepositoryCustom;

/**
 * @author Joao Victor
 *
 */
public interface ProjectRepository extends CrudRepository<Project, Long>, ProjectRepositoryCustom{
	
}
