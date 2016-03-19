/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * @author Joao Victor
 *
 */
@Repository
public class ItemChageLogRepositoryImpl implements ItemChangeLogRepositoryCustom {

	private ItemChangeLogRepository repository;
	
	private EntityManager entityManager;
	
	@Autowired
	public ItemChageLogRepositoryImpl(ItemChangeLogRepository repository, EntityManager entityManager){
		this.entityManager = entityManager;
		this.repository = repository;
	}
	
	@Override
	public List<ItemChangeLog> findAllChangeLogRepositoryItem(
			RepositoryItem repositoryItem) {
		
		String hql = "SELECT changeLog FROM ItemChangeLog AS changeLog "
				+ "INNER JOIN changeLog.revisionItem AS revisionItem "
				+ "INNER JOIN revisionItem.repositoryItem AS repositoryItem "
				+ "WHERE repositoryItem = :repositoryItem ";
		
		Query query = entityManager.createQuery(hql);
		
		query.setParameter("repositoryItem", repositoryItem);
		
		return query.getResultList();
	}

	public ItemChangeLog save(ItemChangeLog changeLog){
		return repository.save(changeLog);
	}
}
