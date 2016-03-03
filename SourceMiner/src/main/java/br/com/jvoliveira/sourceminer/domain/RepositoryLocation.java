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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocationType;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryVersionManager;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="repository_location")
public class RepositoryLocation implements ObjectDB{

	@Id
	@Column(name="id_repository_location")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="url")
	private String url;
	
	@Column(name="enable")
	private boolean enable;
	
	@Enumerated(EnumType.STRING)
    @Column(name="location")
	private RepositoryLocationType locationType;
	
	@Enumerated(EnumType.STRING)
    @Column(name="version_manager")
	private RepositoryVersionManager versionManager;
	
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RepositoryLocationType getLocationType() {
		return locationType;
	}

	public String getLocationName() {
		return getLocationType().getLocation();
	}
	
	public void setLocation(RepositoryLocationType location) {
		this.setLocationType(location);
	}

	public RepositoryVersionManager getVersionManager() {
		return versionManager;
	}
	
	public String getVersionManagerName() {
		return versionManager.getVersionManager();
	}

	public void setVersionManager(RepositoryVersionManager versionManager) {
		this.versionManager = versionManager;
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

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setLocationType(RepositoryLocationType locationType) {
		this.locationType = locationType;
	}
	
	public String getEnableDesc(){
		if(enable)
			return "Sim";
		return "Não";
	}
	
}
