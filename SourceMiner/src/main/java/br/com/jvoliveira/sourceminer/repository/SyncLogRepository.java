/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;

/**
 * @author Joao Victor
 *
 */
public interface SyncLogRepository extends CrudRepository<RepositorySyncLog, Long>{

	RepositorySyncLog findFirstByProjectOrderByIdDesc(Project project);
	
	Collection<RepositorySyncLog> findByProjectOrderByIdDesc(Project project);
	
}
