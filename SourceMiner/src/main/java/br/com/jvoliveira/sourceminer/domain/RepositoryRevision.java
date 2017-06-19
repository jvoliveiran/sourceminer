/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="repository_revision")
public class RepositoryRevision implements ObjectDB,Comparable<RepositoryRevision>{

	@Id
	@Column(name="id_repository_revision")
	@SequenceGenerator(name="repository_revision_seq", sequenceName="repository_revision_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_revision_seq")
	private Long id;
	
	@Column(name="revision")
	private String revision;
	
	@Column(name="comment", length=3000)
	private String comment;
	
	@Column(name="author")
	private String author;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_revision")
	private Date dateRevision;
	
	@OneToMany(mappedBy="repositoryRevision")
	private List<RepositoryRevisionItem> revisionItem;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@ManyToOne
	@JoinColumn(name="id_task")
	private Task task;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	public RepositoryRevision(){
		
	}
	
	public RepositoryRevision(Long id, String revision){
		this.id = id;
		this.revision = revision;
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

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDateRevision() {
		return dateRevision;
	}

	public void setDateRevision(Date dateRevision) {
		this.dateRevision = dateRevision;
	}
	
	public String getFullDescription(){
		return this.revision.toString() + " - " + this.comment;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<RepositoryRevisionItem> getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(List<RepositoryRevisionItem> revisionItem) {
		this.revisionItem = revisionItem;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public int compareTo(RepositoryRevision o) {
		if(this.id.longValue() == o.id.longValue())
			return 0;
		return -1;
	}
}
