/**
 * 
 */
package br.com.jvoliveira.arq.domain;

/**
 * Interface que representa todos os objetos persistidos no banco
 * 
 * @author Joao Victor
 * 
 */
public interface ObjectDB {

	void setId(Long id);
	
	Long getId();
	
}
