/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.repository.RepositoryLocationRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RepositoryLocationService extends AbstractArqService<RepositoryLocation>{

	@Autowired
	public RepositoryLocationService(RepositoryLocationRepository repository){
		this.repository = repository;
	}
	
	@Override
	protected RepositoryLocation beforeCreate(RepositoryLocation obj) {
		String url =  obj.getUrl();
		if(obj.getLocationType().isLocal())
			url = "file://" + url;
		
		obj.setUrl(url);
		obj.setEnable(true);
		
		return obj;
	}
	
	public List<RepositoryLocation> getAllEnable(){
		return ((RepositoryLocationRepository) repository).findByEnable(true);
	}
}
