/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
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
		
		try{
			return (String) query.getSingleResult();
		}catch(NoResultException noResultExp){
			return "-1";
		}
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
	
	public List<RepositoryItem> findItemsChangedInRevisions(Project project, List<String> revisions){
		String hql = "SELECT DISTINCT repoItem "
				+ " FROM RepositoryRevisionItem repRevItem "
				+ " JOIN repRevItem.repositoryRevision repoRev "
				+ " JOIN repRevItem.repositoryItem repoItem "
				+ " WHERE repoItem.project.id = :idProject "
				+ " AND repoRev.revision IN (:revisions) ";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("idProject", project.getId());
		query.setParameter("revisions",revisions);
		
		return query.getResultList();
	}

	@Override
	public List<RepositoryRevisionItem> findItemsModifiedPreviouslyIn(Collection<Long> ids, RepositoryRevision revision) {
		String hql = "SELECT repRevItem "
				+ " FROM RepositoryRevisionItem repRevItem "
				+ " JOIN repRevItem.repositoryRevision repoRev "
				+ " JOIN repRevItem.repositoryItem repoItem "
				+ " WHERE repoItem.project.id = :idProject "
				+ " AND repoItem.id IN (:ids) "
				+ " AND repoRev.id <> :idRevision "
				+ " AND repoRev.dateRevision < :dateRevision ";
		if(revision.getTask() != null)
			hql += " AND (repoRev.task.id <> :idTask OR repoRev.task IS NULL )";
		hql += " ORDER BY repRevItem.repositoryRevision.dateRevision DESC ";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("ids",ids);
		query.setParameter("idProject", revision.getProject().getId());
		query.setParameter("idRevision", revision.getId());
		query.setParameter("dateRevision", revision.getDateRevision());
		if(revision.getTask() != null)
			query.setParameter("idTask", revision.getTask().getId());
		
		return query.getResultList();
	}
	
}
