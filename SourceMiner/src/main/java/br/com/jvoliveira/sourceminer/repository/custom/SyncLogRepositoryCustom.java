/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Project;

/**
 * @author João Victor
 *
 */
public interface SyncLogRepositoryCustom {

	List<String> findTwoMostRecentRevisions(Project project);
	
	Integer countSyncLogByProject(Project project);
}
