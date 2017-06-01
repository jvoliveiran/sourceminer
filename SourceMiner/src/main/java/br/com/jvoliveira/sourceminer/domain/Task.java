/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="task")
public class Task implements ObjectDB{

	@Id
	@Column(name="id_task")
	@SequenceGenerator(name="task_seq",sequenceName="task_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="task_seq")
	private Long id;
	
	@OneToMany(mappedBy="task")
	private Set<RepositoryRevision> revisions;
	
	@Column(name="number")
	private Long number;
	
	@Column(name="title")
	private String title;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="feature_task",
			joinColumns = @JoinColumn(name="id_task"),
			inverseJoinColumns = @JoinColumn(name="id_feature"))
	private Set<Feature> features;
	
	@Column(name="create_at")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date createAt;
	
	@Column(name="update_at")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date updateAt;
	
	public Task(){
		setRevisions(new HashSet<>());
	}
	
	public Task(String number){
		this.setNumber(Long.valueOf(number));
		setRevisions(new HashSet<>());
	}
	
	public Task(String number, RepositoryRevision revision){
		this.setNumber(Long.valueOf(number));
		setRevisions(new HashSet<>());
		this.features = new HashSet<>();
		this.project = revision.getProject();
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

	public Set<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<RepositoryRevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(Set<RepositoryRevision> revisions) {
		this.revisions = revisions;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}
	
}
