/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */

@Entity
@Table(name="project")
public class Project implements ObjectDB{

	@Id
	@Column(name="id_project")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="path")
	private String path;
	
	@ManyToOne
	@JoinColumn(name="id_repository_location")
	private RepositoryLocation repositoryLocation;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	@OneToOne
	@JoinColumn(name="id_repository_sync_log")
	private RepositorySyncLog lastSync;
	
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

	public RepositoryLocation getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(RepositoryLocation repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public RepositorySyncLog getLastSync() {
		return lastSync;
	}

	public void setLastSync(RepositorySyncLog lastSync) {
		this.lastSync = lastSync;
	}

	public String getSyncText(){
		try{
			return this.lastSync.getCreateAt().toString() + " (Head revision: " + this.lastSync.getHeadRevision() + ")";
		}catch(NullPointerException e){
			return "Não sincronizado";
		}
	}
	
	public boolean isGit(){
		return this.repositoryLocation.getVersionManager().isGITManager();
	}
	
	public boolean isSVN(){
		return this.repositoryLocation.getVersionManager().isSVNManager();
	}
}
