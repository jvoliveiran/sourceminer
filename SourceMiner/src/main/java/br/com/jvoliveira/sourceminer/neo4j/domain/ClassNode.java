/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * Nós do grafo que serão representações referentes à RepositoryItem
 * 
 * @author João Victor
 *
 */
@NodeEntity
public class ClassNode implements ObjectDB{

	@GraphId
	private Long id;
	
	private Long repositoryItemId;
	
	private Long projectId;
	
	private String name;
	
	private String fullPath;
	
	@Relationship(type = "CALLS", direction = Relationship.OUTGOING)
	private List<MethodCall> methodsCalled = new ArrayList<>();
	
	@Relationship(type = "ACCESS", direction = Relationship.UNDIRECTED)
	private List<FieldAccess> fieldAccess = new ArrayList<>();
	
	public ClassNode(){
		
	}
	
	public ClassNode(Long repositoryItemId, String name){
		this.repositoryItemId = repositoryItemId;
		this.name = name;
	}
	
	public ClassNode(RepositoryItem item, Project project){
		this.repositoryItemId = item.getId();
		this.name = item.getName();
		this.fullPath = item.getFullPath();
		if(item.getGraphNodeId() != null)
			this.id = item.getGraphNodeId();
		this.projectId = project.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRepositoryItemId() {
		return repositoryItemId;
	}

	public void setRepositoryItemId(Long repositoryItemId) {
		this.repositoryItemId = repositoryItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public List<MethodCall> getMethodsCalled() {
		return methodsCalled;
	}

	public void setMethodsCalled(List<MethodCall> methodsCalled) {
		this.methodsCalled = methodsCalled;
	}

	@Override
	public void setCreateAt(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getCreateAt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpdateAt(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getUpdateAt() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FieldAccess> getFieldAccess() {
		return fieldAccess;
	}

	public void setFieldAccess(List<FieldAccess> fieldAccess) {
		this.fieldAccess = fieldAccess;
	}
	
}
