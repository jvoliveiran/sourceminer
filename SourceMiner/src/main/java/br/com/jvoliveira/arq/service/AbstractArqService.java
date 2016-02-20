/**
 * 
 */
package br.com.jvoliveira.arq.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.jvoliveira.arq.dao.GenericDAO;
import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.arq.utils.DateUtils;
import br.com.jvoliveira.security.domain.User;

/**
 * @author Joao Victor
 *
 */
public class AbstractArqService<T extends ObjectDB> {

	protected CrudRepository<T, Long> repository;
	
	@Autowired
	private GenericDAO dao;
	
	protected GenericDAO getDAO(){
		return this.dao;
	}
	
	/**
	 * Retorna um objeto buscando pelo seu Id
	 * @param id
	 * @return
	 */
	public T getOneById(Long id) {
		return repository.findOne(id);
	}

	/**
	 * Retorna todos os objetos
	 * @return
	 */
	public List<T> getAll() {
		return (List<T>) repository.findAll();
	}
	
	/**
	 * Persiste um objeto na base de dados
	 * @param obj
	 * @return
	 */
	public T persist(T obj){
		
		if(obj.getId() == null)
			obj = createObject(obj);
		else
			obj = updateObject(obj);
		
		return obj;
	}

	private T updateObject(T obj) {
		obj = beforeUpdate(obj);
		obj.setUpdateAt(DateUtils.now());
		obj = repository.save(obj);
		obj = afterUpdate(obj);
		
		return obj;
	}

	protected T afterUpdate(T obj) {
		// TODO Auto-generated method stub
		return obj;
	}

	protected T beforeUpdate(T obj) {
		// TODO Auto-generated method stub
		return obj;
	}

	private T createObject(T obj) {
		obj = beforeCreate(obj);
		obj.setCreateAt(DateUtils.now());
		obj = repository.save(obj);
		obj = afterCreate(obj);
		
		return obj;
	}

	protected T afterCreate(T obj) {
		// TODO Auto-generated method stub
		return obj;
	}

	protected T beforeCreate(T obj) {
		// TODO Auto-generated method stub
		return obj;
	}

	/**
	 * Remove uma instancia do banco, identificada pelo ID
	 * @param id
	 */
	public void remove(Long id) {
		repository.delete(id);
		
	}
	
	protected User getUsuarioLogado(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
}
