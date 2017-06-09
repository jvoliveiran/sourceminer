/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.ArrayList;
import java.util.Comparator;
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
 * @author Joao Victor
 *
 */

@Entity
@Table(name="repository_item")
public class RepositoryItem implements ObjectDB{

	@Id
	@Column(name="id_repository_item")
	@SequenceGenerator(name="repository_item_seq", sequenceName="repository_item_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="repository_item_seq")
	private Long id;
	
	@Column(name="graph_node_id")
	private Long graphNodeId;
	
	@Column(name="name", length=500)
	private String name;
	
	@Column(name="path", length=800)
	private String path;
	
	@Column(name="extension")
	private String extension;
	
	@OneToMany(mappedBy="repositoryItem")
	private List<RepositoryRevisionItem> revisionItem;
	
	@ManyToOne
	@JoinColumn(name="id_project")
	private Project project;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	public RepositoryItem(){
		
	}
	
	public RepositoryItem(Long id, String path){
		this.id = id;
		this.path = path;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public String getFullPath(){
		return this.path;
	}

	public List<RepositoryRevisionItem> getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(List<RepositoryRevisionItem> revisionItem) {
		this.revisionItem = revisionItem;
	}

	public List<String> getAllRevisionsNumber(){
		List<String> revisionsNumber = new ArrayList<>();
		if(this.revisionItem != null && this.revisionItem.size() > 0){
			for(RepositoryRevisionItem revisionItem : this.revisionItem)
				revisionsNumber.add(revisionItem.getRepositoryRevision().getRevision());
			
			Comparator<String> comparator = (revision1, revision2) -> revision1.compareTo(revision2);
			revisionsNumber.sort(comparator.reversed());
		}
		return revisionsNumber;
	}

	public Long getGraphNodeId() {
		return graphNodeId;
	}

	public void setGraphNodeId(Long graphNodeId) {
		this.graphNodeId = graphNodeId;
	}
}
