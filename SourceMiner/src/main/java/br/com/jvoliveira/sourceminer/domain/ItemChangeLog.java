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

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.sourceminer.domain.enums.ChangeFileType;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="item_change_log")
public class ItemChangeLog implements ObjectDB{

	@Id
	@Column(name="id_item_change_log")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_item_asset")
	private ItemAsset asset;
	
	@Column(name="name")
	private String name;
	
	@Column(name="signature", length=1000)
	private String signature;
	
	@ManyToOne
	@JoinColumn(name="id_repository_revision_item")
	private RepositoryRevisionItem revisionItem;
	
	@Enumerated(EnumType.STRING)
	@Column(name="change_file_type")
	private ChangeFileType changeType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	public ItemChangeLog(){
		
	}
	
	public ItemChangeLog(ChangeFileType changeType){
		this.changeType = changeType;
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

	public ChangeFileType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeFileType changeType) {
		this.changeType = changeType;
	}
	
	public String getChangeTypeDescription(){
		return this.changeType.getDescription();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ItemAsset getAsset() {
		return asset;
	}

	public void setAsset(ItemAsset asset) {
		this.asset = asset;
	}
	
	public String getChangeLogDesc(){
		return this.asset.getAssetTypeDesc() + " " 
				+ this.changeType.getDescription() + " - "
				+ this.signature;
	}

	public RepositoryRevisionItem getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(RepositoryRevisionItem revisionItem) {
		this.revisionItem = revisionItem;
	}
	
	public Long getRevision(){
		try{
			return this.revisionItem.getRepositoryRevision().getRevision();
		}catch(NullPointerException e){
			return 0L;
		}
	}
		
	public String getBackgroundColorHexa(){
		if(this.changeType.isAdd())
			return getHexaColorAdd();
		else if(this.changeType.isDelete())
			return getHexaColorDelete();
		else
			return getHexaColorUpdate();
	}
	
	private String getHexaColorAdd(){
		return "#70db70";
	}
	
	private String getHexaColorDelete(){
		return "#ff4d4d";
	}
	
	private String getHexaColorUpdate(){
		return "#ffa64d";
	}
}
