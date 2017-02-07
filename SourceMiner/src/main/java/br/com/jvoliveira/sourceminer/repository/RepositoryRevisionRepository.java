/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryRevisionRepository extends CrudRepository<RepositoryRevision, Long>{
	
	List<RepositoryRevision> findByProject(Project project);
	
	List<RepositoryRevision> findTop10ByProjectOrderByIdDesc(Project project);
	
	RepositoryRevision findByProjectAndRevision(Project project, String revision);
	
	@Query("SELECT count(rr) FROM RepositoryRevision rr WHERE rr.project = ?1")
	Integer countTotalItensByProject(Project project);

}
