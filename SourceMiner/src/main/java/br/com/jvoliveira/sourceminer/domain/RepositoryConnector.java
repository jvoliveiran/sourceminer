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
import br.com.jvoliveira.security.domain.User;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryVersionManager;

/**
 * @author Joao Victor
 *
 */
@Entity
@Table(name="repository_connector")
public class RepositoryConnector implements ObjectDB {
	@Id
	@Column(name = "id_repository_connector")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;
	
	@Column(name="url")
	private String url;
	
	@Enumerated(EnumType.STRING)
    @Column(name="location")
	private RepositoryLocation location;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RepositoryLocation getLocation() {
		return location;
	}

	public String getLocationName() {
		return location.getLocation();
	}
	
	public void setLocation(RepositoryLocation location) {
		this.location = location;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
