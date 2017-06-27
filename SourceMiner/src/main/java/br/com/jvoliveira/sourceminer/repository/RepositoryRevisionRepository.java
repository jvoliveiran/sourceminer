/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.repository.custom.RepositoryRevisionRepositoryCustom;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryRevisionRepository extends CrudRepository<RepositoryRevision, Long>, RepositoryRevisionRepositoryCustom{
	
	List<RepositoryRevision> findByProject(Project project);
	
	List<RepositoryRevision> findTop10ByProjectOrderByDateRevisionDesc(Project project);
	
	List<RepositoryRevision> findByProjectOrderByDateRevisionDesc(Project project, Pageable pageable);
	
	RepositoryRevision findByProjectAndRevision(Project project, String revision);
	
	@Query("SELECT count(rr) FROM RepositoryRevision rr WHERE rr.project = ?1")
	Integer countTotalItensByProject(Project project);

}
