package br.com.jvoliveira.arq.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author João Victor
 *
 */
@Repository
public class GenericDAOImpl implements GenericDAO{

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjectDB> List<T> findAll(Class<T> entityClass) {
		return ((List<T>) entityManager.createQuery("SELECT obj FROM " + entityClass.getSimpleName() +" obj ").getResultList());
	}

	@Override
	public <T extends ObjectDB> T findByPrimaryKey(Long id, Class<T> entityClass) {
		return (T) entityManager.find(entityClass, id);
	}
	
	@Transactional("transactionManager")
	@Override
	public <T extends ObjectDB> T update(T obj){
		return entityManager.merge(obj);
	}
	
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}
	
}
