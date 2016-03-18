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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.jvoliveira.sourceminer.domain.enums.AssetType;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="item_asset")
public class ItemAsset implements GenericAsset{
	
	@Id
	@Column(name = "id_item_asset")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="signature")
	private String signature;
	
	@Enumerated(EnumType.STRING)
	@Column(name="asset_type")
	private AssetType assetType;
	
	@ManyToOne
	@JoinColumn(name="id_repository_item")
	private RepositoryItem repositoryItem;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;
	
	@Transient
	private ItemChangeLog itemChageLog;
	
	@Lob
	@Column(name="value", length=10000)
	private String value;

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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSignature() {
		return this.signature;
	}

	@Override
	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public AssetType getAssetType() {
		return this.assetType;
	}

	@Override
	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getAssetTypeDescription(){
		return this.assetType.getDescription();
	}

	public RepositoryItem getRepositoryItem() {
		return repositoryItem;
	}

	public void setRepositoryItem(RepositoryItem repositoryItem) {
		this.repositoryItem = repositoryItem;
	}

	public ItemChangeLog getItemChageLog() {
		return itemChageLog;
	}

	public void setItemChageLog(ItemChangeLog itemChageLog) {
		this.itemChageLog = itemChageLog;
	}
	
	@Override
	public boolean equals(Object anotherAsset){
		return this.toString().equals(anotherAsset.toString());
	}
	
	@Override
	public String toString(){
		return this.signature + " # " + getAssetTypeDescription();
	}
	
	public boolean isMethodAsset(){
		return this.assetType.equals(AssetType.METHOD);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean hasSameBodyMethod(ItemAsset asset){
		if(!this.isMethodAsset() || !asset.isMethodAsset())
			return false;
		
		return this.getValue().equals(asset.getValue());
	}
}
