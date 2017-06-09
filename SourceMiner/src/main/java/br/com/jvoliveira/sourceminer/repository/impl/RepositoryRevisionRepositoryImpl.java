/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.repository.custom.RepositoryRevisionRepositoryCustom;

/**
 * @author João Victor
 *
 */
@Repository
public class RepositoryRevisionRepositoryImpl implements RepositoryRevisionRepositoryCustom{
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public RepositoryRevision findByProjectAndRevisionOptimized(Project project, String revision) {
		String hql = " SELECT NEW RepositoryRevision(id, revision) "
				+ " FROM RepositoryRevision repositoryRev "
				+ " WHERE repositoryRev.revision = :revision "
				+ " AND repositoryRev.project.id = :idProject ";
		
		try{
			TypedQuery<RepositoryRevision> query = entityManager.createQuery(hql, RepositoryRevision.class).setFirstResult(0).setMaxResults(1);
			query.setParameter("revision", revision);
			query.setParameter("idProject", project.getId());
		
			return query.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}
	}
	
}
