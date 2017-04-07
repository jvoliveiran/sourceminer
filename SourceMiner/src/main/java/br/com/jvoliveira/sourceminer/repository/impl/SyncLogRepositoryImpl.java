/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.repository.custom.SyncLogRepositoryCustom;

/**
 * @author João Victor
 *
 */
@Repository
public class SyncLogRepositoryImpl implements SyncLogRepositoryCustom{

	private EntityManager manager;
	
	@Autowired
	public void setManager(EntityManager manager){
		this.manager = manager;
	}
	
	@Override
	public List<String> findTwoMostRecentRevisions(Project project) {
		String sql = "SELECT head_revision "
				+ " FROM( "
				+ "		SELECT DISTINCT ON(head_revision) head_revision, id_repository_sync_log "
				+ "		FROM repository_sync_log "
				+ "		WHERE id_project = :idProject " 
				+ "	)  subQuery "
				+ "	ORDER BY id_repository_sync_log DESC LIMIT 2 ";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("idProject", project.getId());
		
		return query.getResultList();
	}

	@Override
	public Integer countSyncLogByProject(Project project) {
		String sql = "SELECT count(*) FROM repository_sync_log WHERE id_project = :idProject";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("idProject", project.getId());
		return query.getFirstResult();
	}

}
