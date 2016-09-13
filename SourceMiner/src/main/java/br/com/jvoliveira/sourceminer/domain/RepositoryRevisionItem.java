/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.sourceminer.domain.enums.CommitType;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="repository_revision_item")
public class RepositoryRevisionItem implements ObjectDB{

	@Id
	@Column(name="id_repository_revision_item")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@ManyToOne
	@JoinColumn(name="id_repository_item")
	private RepositoryItem repositoryItem;
	
	@ManyToOne
	@JoinColumn(name="id_repository_revision")
	private RepositoryRevision repositoryRevision;
	
	@Enumerated
	@Column(name="commit_type")
	private CommitType commitType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	public RepositoryRevisionItem(){
		
	}
	
	public RepositoryRevisionItem(RepositoryItem item){
		this.repositoryItem = item;
	}
	
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public RepositoryItem getRepositoryItem() {
		return repositoryItem;
	}

	public void setRepositoryItem(RepositoryItem repositoryItem) {
		this.repositoryItem = repositoryItem;
	}

	public RepositoryRevision getRepositoryRevision() {
		return repositoryRevision;
	}

	public void setRepositoryRevision(RepositoryRevision repositoryRevision) {
		this.repositoryRevision = repositoryRevision;
	}

	public CommitType getCommitType() {
		return commitType;
	}

	public String getCommitTypeDescription(){
		return this.commitType.getDescription();
	}
	
	public void setCommitType(CommitType commitType) {
		this.commitType = commitType;
	}
	
	public boolean existFileInRepository(){
		return this.commitType.isAdded() || this.commitType.isModified();
	}
}

