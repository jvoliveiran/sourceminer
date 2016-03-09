/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.sourceminer.domain.enums.ChangeFileType;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="method_changelog")
public class MethodChangeLog implements AssetChangeLog{
	
	@Id
	@Column(name="id_method_changelog")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="signature")
	private String signature;
	
	@Enumerated(EnumType.STRING)
	@Column(name="change_file_type")
	private ChangeFileType changeFileType;
	
	@ManyToOne
	@JoinColumn(name="id_revision_item")
	private RepositoryRevisionItem revisionItem;
	
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

	@Override
	public String getSignature() {
		return this.signature;
	}

	@Override
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ChangeFileType getChangeFileType() {
		return changeFileType;
	}

	public void setChangeFileType(ChangeFileType changeFileType) {
		this.changeFileType = changeFileType;
	}
	
	public String getDescriptionChangeFileType(){
		return this.changeFileType.getDescription();
	}

	public RepositoryRevisionItem getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(RepositoryRevisionItem revisionItem) {
		this.revisionItem = revisionItem;
	}
	
}
