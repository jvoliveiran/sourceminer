/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;

/**
 * @author Joao Victor
 *
 */
@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom{

	private EntityManager entityManager;
	
	@Autowired
	public ProjectRepositoryImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Project> findByRepositoryLocation(RepositoryLocation location) {
		String hql = "SELECT project FROM ProjectConfiguration AS config "
				+ " JOIN config.project AS project "
				+ " WHERE project.repositoryLocation.id = :location "
				+ " AND config.published = true ";
		
		Query query = entityManager.createQuery(hql);
		
		query.setParameter("location", location.getId());
		
		return query.getResultList();
	}

}
