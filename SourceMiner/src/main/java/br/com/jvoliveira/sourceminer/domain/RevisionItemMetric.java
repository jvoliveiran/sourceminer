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
@Table(name="revision_item_metric")
public class RevisionItemMetric implements ObjectDB{

	@Id
	@Column(name="id_revision_item_metric")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_repository_revision_item")
	private RepositoryRevisionItem revisionItem;
	
	@ManyToOne
	@JoinColumn(name="id_metric")
	private Metric metric;
	
	@Column(name="value")
	private Double value;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@Column(name="update_at")
	@Temporal(TemporalType.DATE)
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

	public RepositoryRevisionItem getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(RepositoryRevisionItem revisionItem) {
		this.revisionItem = revisionItem;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
