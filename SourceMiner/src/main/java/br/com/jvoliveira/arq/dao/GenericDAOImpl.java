package br.com.jvoliveira.arq.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
	@Transactional("transactionManager")
	@Override
	public <T extends ObjectDB> T updateField(T obj,String field, Object value){
		String className = obj.getClass().getName();
		String hql = "UPDATE " + className + " SET " + field + " = :newValue WHERE id = :objId ";
		Query query = entityManager.createQuery(hql);
		
		query.setParameter("newValue", value);
		query.setParameter("objId", obj.getId());
		
		query.executeUpdate();
		return obj;
	}
	
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}
	
}
