/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.User;

/**
 * @author Joao Victor
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{

	User findOneByUsername(String username);
}
