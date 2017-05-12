/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.Task;

/**
 * @author João Victor
 *
 */
public interface TaskRepository extends CrudRepository<Task, Long>{

	Collection<Task> findByNumberAndProject(Long number, Project project);
	
}
