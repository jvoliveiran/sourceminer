/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="project_configuration")
public class ProjectConfiguration implements ObjectDB{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_project_configuration")
	private Long id;
	
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name="id_project")
	private Project project;
	
	@Column(name="join_paren_branch")
	private boolean joinParentBrach;
	
	@Column(name="sync_change_log")
	private boolean syncChangeLog;
	
	@Column(name="full_asset_sync")
	private boolean fullAssetSync;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	public ProjectConfiguration() {
		defaultConfiguration();
	}
	
	public ProjectConfiguration(Project project) {
		defaultConfiguration();
		this.project = project;
	}
	
	private void defaultConfiguration(){
		this.joinParentBrach = false;
		this.syncChangeLog = false;
		this.fullAssetSync = false;
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

	public boolean isJoinParentBrach() {
		return joinParentBrach;
	}

	public void setJoinParentBrach(boolean joinParentBrach) {
		this.joinParentBrach = joinParentBrach;
	}

	public boolean isSyncChangeLog() {
		return syncChangeLog;
	}

	public void setSyncChangeLog(boolean syncChangeLog) {
		this.syncChangeLog = syncChangeLog;
	}

	public boolean isFullAssetSync() {
		return fullAssetSync;
	}

	public void setFullAssetSync(boolean fullAssetSync) {
		this.fullAssetSync = fullAssetSync;
	}

	
	
}
