/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryItemRepository extends CrudRepository<RepositoryItem, Long>{

	List<RepositoryItem> findByProject(Project project);
	
	List<RepositoryItem> findTop10ByProjectOrderByIdDesc(Project project);
}
