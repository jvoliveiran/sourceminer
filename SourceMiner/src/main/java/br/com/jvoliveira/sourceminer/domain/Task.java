/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;
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
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_repository_revision")
	private RepositoryRevision repositoryRevision;
	
	@Column(name="number")
	private Long number;
	
	@Column(name="title")
	private String title;
	
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
		
	}
	
	public Task(String number){
		this.number = Long.valueOf(number);
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
	
}
