/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */

@Entity
@Table(name="repository_item")
public class RepositoryItem implements ObjectDB{

	@Id
	@Column(name="id_repository_item")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="path")
	private String path;
	
	@Column(name="extension")
	private String extension;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "repository_revision_item",
			joinColumns = { @JoinColumn(name = "id_repository_item") },
			inverseJoinColumns = { @JoinColumn(name = "id_repository_revision") }
			)
	private List<RepositoryRevision> repositoryRevision = new ArrayList<RepositoryRevision>();
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setCreateAt(Date date) {
		this.createAt = date;
	}

	@Override
	public Date getCreateAt() {
		return this.createAt;
	}

	@Override
	public void setUpdateAt(Date date) {
		this.updateAt = date;
	}

	@Override
	public Date getUpdateAt() {
		return this.updateAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public List<RepositoryRevision> getRepositoryRevision() {
		return repositoryRevision;
	}

	public void setRepositoryRevision(List<RepositoryRevision> repositoryRevision) {
		this.repositoryRevision = repositoryRevision;
	}
	
}
