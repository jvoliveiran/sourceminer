/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	@Autowired
	public ProjectService(ProjectRepository repository, RepositoryConnectionSession connection){
		this.repository = repository;
		this.connection = connection;
	}
	
	public List<Project> getAllByConnector(){
		if(connection.getConnection() != null){
			RepositoryConnector connector = connection.getConnection().getConnector();
			return ((ProjectRepository) repository).findByRepositoryConnector(connector);			
		}
		return null;
	}
	
	public List<RepositoryConnector> getAllConnectors(){
		return getDAO().findAll(RepositoryConnector.class);
	}
	
	public List<RepositoryItem> getAllItensInProject(Project project){
		if(this.connection.getConnection() != null)
			return this.connection.getConnection().getAllProjectItens(project);
		
		return null;
	}
	
	public List<RepositoryRevision> getAllRevisionsInProject(Project project){
		/*
		 * TODO:
		 * Buscar todas as revisões no banco ordenado pelo número em ordem decrescente.
		 * Buscar no repositório pela revisão encontrada no banco e pelo path do projeto, até a head-revision.
		 * Descartar o primeiro valor, pois já está no banco, e sincronizar o restante.
		 */
		if(this.connection.getConnection() != null)
			return this.connection.getConnection().getAllProjectRevision(project);
		
		return null;
	}
}
