/**
 * 
 */
package br.com.jvoliveira.security.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.security.domain.User;

/**
 * @author Joao Victor
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{

	User findOneByUsername(String username);
}
