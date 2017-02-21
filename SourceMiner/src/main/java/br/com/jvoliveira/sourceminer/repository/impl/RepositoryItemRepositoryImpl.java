/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.util.List;

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
	public String findLastRevisionInFile(String filePath, Long idProject){
		String hql = "SELECT repositoryRevision.revision FROM RepositoryRevisionItem repositoryRevisionItem "
				+ " INNER JOIN repositoryRevisionItem.repositoryRevision AS repositoryRevision"
				+ " INNER JOIN repositoryRevisionItem.repositoryItem AS repositoryItem"
				+ " WHERE  repositoryItem.path LIKE :filePath AND repositoryRevisionItem.project.id = :idProject ";
		
		Query query = entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1);
		query.setParameter("filePath", filePath);
		query.setParameter("idProject", idProject);
		
		return (String) query.getSingleResult();
	}
	
	@Override
	public RepositoryItem findItemByFullPath(String importPath, Project project, String extension) {
		try{
			importPath = generateImportPath(importPath, project, extension);
			
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
	
	private String generateImportPath(String importPath, Project project, String extension){
		String fullPath = importPath.replace(".", "/");
		if(project.isGit())
			fullPath = project.getName() + "/%/" + fullPath + "." + extension;
		else
			fullPath = project.getPath() + "/%/" + fullPath + "." + extension;
		return fullPath;
	}

	@Autowired
	public void setEntityManager(EntityManager manager){
		this.entityManager = manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RepositoryItem> findItemWithoutNode(Project project) {
		String hql = " FROM RepositoryItem WHERE graphNodeId IS NULL AND project.id = :idProject ";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("idProject", project.getId());
		
		return query.getResultList();
	}
	
}
