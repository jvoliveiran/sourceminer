/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;

/**
 * @author Joao Victor
 *
 */
public interface ProjectConfigurationRepository extends CrudRepository<ProjectConfiguration, Long>{
	
	ProjectConfiguration findOneByProject(Project project);

}
