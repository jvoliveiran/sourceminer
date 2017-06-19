package br.com.jvoliveira.arq.dao;

import java.util.List;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * DAO genérico da arquitetura
 * 
 * @author Joao Victor
 *
 */
public interface GenericDAO {

	<T extends ObjectDB> List<T> findAll(Class<T> entityClass);
	
	<T extends ObjectDB> T findByPrimaryKey(Long id, Class<T> entityClass);
	
	<T extends ObjectDB> T update(T obj);

	<T extends ObjectDB> T updateField(T obj, String field, Object value);
	
}
