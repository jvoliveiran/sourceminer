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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="project_configuration")
public class ProjectConfiguration implements ObjectDB{

	@Id
	@SequenceGenerator(name="project_configuration_seq",sequenceName="project_configuration_seq",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="project_configuration_seq")
	@Column(name="id_project_configuration")
	private Long id;
	
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name="id_project")
	private Project project;
	
	@Column(name="published")
	private boolean published;
	
	@Column(name="join_paren_branch")
	private boolean joinParentBrach;
	
	@Column(name="sync_change_log")
	private boolean syncChangeLog;
	
	@Column(name="full_asset_sync")
	private boolean fullAssetSync;
	
	@ManyToOne
	@JoinColumn(name="id_metric_case")
	private MetricCase metricCase;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	@Transient
	private String syncStartRevision;
	
	@Transient
	private String syncEndRevision;
	
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
		this.published = false;
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getSyncStartRevision() {
		return syncStartRevision;
	}

	public void setSyncStartRevision(String syncStartRevision) {
		this.syncStartRevision = syncStartRevision;
	}

	public String getSyncEndRevision() {
		return syncEndRevision;
	}

	public void setSyncEndRevision(String syncEndRevision) {
		this.syncEndRevision = syncEndRevision;
	}

	public MetricCase getMetricCase() {
		return metricCase;
	}

	public void setMetricCase(MetricCase metricCase) {
		this.metricCase = metricCase;
	}

	
	
}
