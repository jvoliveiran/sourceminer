/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.Role;
import br.com.jvoliveira.sourceminer.domain.User;
import br.com.jvoliveira.sourceminer.repository.UserRepository;

/**
 * @author Joao Victor
 *
 */

@Service
public class UserService extends AbstractArqService<User> implements UserDetailsService{
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.repository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails userDetails = ((UserRepository) repository).findOneByUsername(username);
		
		if(userDetails == null)
			throw new UsernameNotFoundException("Usuario não encontrado!");
		
		return (User) userDetails;
	}
	
	public List<Role> getAllRoles(){
		return (List<Role>) getDAO().findAll(Role.class);
	}
}
