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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="repository_sync_log")
public class RepositorySyncLog implements ObjectDB{

	@Id
	@Column(name="id_repository_sync_log")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="begin_sync")
	private Date beginSync;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="finish_sync")
	private Date finishSync;
	
	@Column(name="head_revision")
	private String headRevision;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="graph_sync_date")
	private Date graphSyncDate;
	
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
		return;
	}

	@Override
	public Date getUpdateAt() {
		return null;
	}

	public String getHeadRevision() {
		return headRevision;
	}

	public void setHeadRevision(String headRevision) {
		this.headRevision = headRevision;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public boolean isGraphOutOfSync(){
		return this.getGraphSyncDate() == null;
	}

	public Date getGraphSyncDate() {
		return graphSyncDate;
	}

	public void setGraphSyncDate(Date graphSyncDate) {
		this.graphSyncDate = graphSyncDate;
	}

	public Date getBeginSync() {
		return beginSync;
	}

	public void setBeginSync(Date beginSync) {
		this.beginSync = beginSync;
	}

	public Date getFinishSync() {
		return finishSync;
	}

	public void setFinishSync(Date finishSync) {
		this.finishSync = finishSync;
	}
	
}
