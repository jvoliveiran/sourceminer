/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectConfigurationService extends AbstractArqService<ProjectConfiguration>{

	@Autowired
	public ProjectConfigurationService(ProjectConfigurationRepository repository){
		this.repository = repository;
	}
	
	public ProjectConfiguration getProjectConfiguration(Long idProject){
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		return ((ProjectConfigurationRepository)repository).findOneByProject(project);
	}
}
