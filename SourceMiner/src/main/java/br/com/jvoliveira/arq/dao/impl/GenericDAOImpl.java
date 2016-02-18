package br.com.jvoliveira.arq.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.arq.dao.GenericDAO;
import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author João Victor
 *
 */
@Repository
public class GenericDAOImpl implements GenericDAO{

	private EntityManager entityManager;
	
	@Autowired
	public GenericDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ObjectDB> List<T> findAll(Class<T> entityClass) {
		return ((List<T>) entityManager.createQuery("SELECT obj FROM " + entityClass.getSimpleName() +" obj ").getResultList());
	}

	@Override
	public <T extends ObjectDB> T findByPrimaryKey(Long id, Class<T> entityClass) {
		return (T) entityManager.find(entityClass, id);
	}
	
}
