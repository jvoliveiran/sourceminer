/**
 * 
 */
package br.com.jvoliveira.arq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.arq.dao.GenericDAO;
import br.com.jvoliveira.arq.domain.ObjectDB;

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
	public T create(T obj){
		repository.save(obj);
		return obj;
	}

	/**
	 * Remove uma instancia do banco, identificada pelo ID
	 * @param id
	 */
	public void remove(Long id) {
		repository.delete(id);
		
	}
	
}
