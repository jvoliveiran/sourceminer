/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;

/**
 * @author Joao Victor
 *
 */
public interface RepositorySyncLogRepository extends CrudRepository<RepositorySyncLog, Long>{

	RepositorySyncLog findByProjectOrderByIdDesc(Project project);
	
}
