/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="metric_case")
public class MetricCase implements ObjectDB{
	
	@Id
	@Column(name="id_metric_case")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy="metricCase")
	private List<CaseMetricItem> caseMetricItems;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@Column(name="update_at")
	@Temporal(TemporalType.DATE)
	private Date updateAt;
	
	public MetricCase(){
		this.caseMetricItems = new ArrayList<CaseMetricItem>();
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

	public List<CaseMetricItem> getCaseMetricItems() {
		return caseMetricItems;
	}

	public void setCaseMetricItems(List<CaseMetricItem> caseMetricItems) {
		this.caseMetricItems = caseMetricItems;
	}

}
