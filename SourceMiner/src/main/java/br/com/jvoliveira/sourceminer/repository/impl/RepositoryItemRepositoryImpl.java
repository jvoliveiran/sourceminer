/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.repository.custom.RepositoryItemRepositoryCustom;

/**
 * @author Joao Victor
 *
 */
@Repository
public class RepositoryItemRepositoryImpl implements RepositoryItemRepositoryCustom{

	private EntityManager entityManager;
	
	@Override
	public Long findLastRevisionInFile(String filePath, Long idProject){
		String hql = "SELECT repositoryRevision.revision FROM RepositoryRevisionItem repositoryRevisionItem "
				+ " INNER JOIN repositoryRevisionItem.repositoryRevision AS repositoryRevision"
				+ " INNER JOIN repositoryRevisionItem.repositoryItem AS repositoryItem"
				+ " WHERE  repositoryItem.path LIKE :filePath AND repositoryRevisionItem.project.id = :idProject ";
		
		Query query = entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1);
		query.setParameter("filePath", filePath);
		query.setParameter("idProject", idProject);
		
		return (Long) query.getSingleResult();
	}
	
	@Override
	public RepositoryItem findItemByFullPath(String importPath, Project project, String extension) {
		try{
			importPath = importPath.replace(".", "/");
			importPath = project.getPath() + "/%/" + importPath + "." + extension;
			
			String hql = " FROM RepositoryItem "
					+ " WHERE path LIKE :path AND project.id = :idProject ";
			
			Query query = entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1);
			
			query.setParameter("path", importPath);
			query.setParameter("idProject", project.getId());
			
			return (RepositoryItem) query.getSingleResult();
		}catch(EmptyResultDataAccessException e){
			return null;
		}catch (NoResultException e) {
			return null;
		}catch (NullPointerException e){
			return null;
		}
	}

	@Autowired
	public void setEntityManager(EntityManager manager){
		this.entityManager = manager;
	}
	
}
