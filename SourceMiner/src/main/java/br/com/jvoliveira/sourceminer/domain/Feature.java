/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author João Victor
 *
 */
@Entity
@Table(name="feature")
public class Feature implements ObjectDB, Comparable<Feature>{

	@Id
	@Column(name="id_feature")
	@SequenceGenerator(name="feature_seq",sequenceName="feature_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="feature_seq")
	private Long id;
	
	@Column(name="path")
	private String path;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@ManyToMany(mappedBy="features")
	private Set<Task> tasks;
	
	@ManyToMany
	@JoinTable(name="feature_item",
			joinColumns=@JoinColumn(name="id_feture"),
			inverseJoinColumns=@JoinColumn(name="id_repository_item"))
	private Set<RepositoryItem> items;
	
	@Column(name="create_at")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date createAt;
	
	@Column(name="update_at")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date updateAt;
	
	public Feature(){
		
	}

	public Feature(String path){
		this.setPath(path);
	}
	
	public Feature(String path, Project project){
		this.setPath(path);
		this.project = project;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<RepositoryItem> getItems() {
		return items;
	}

	public void setItems(Set<RepositoryItem> items) {
		this.items = items;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public int compareTo(Feature o) {
		if(this.id.longValue() == o.getId().longValue())
			return 0;
		else if(this.getPath().equals(o.getPath()) && project.getId().longValue() == o.getProject().getId().longValue())
			return 0;
		return -1;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
