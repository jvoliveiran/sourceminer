package br.com.jvoliveira.sourceminer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority, ObjectDB{

	private static final long serialVersionUID = 0L;

	@Id
	@Column(name = "id_role")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "ativo")
	private Boolean ativo = true;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "role_permission",
			joinColumns = { @JoinColumn(name = "id_role") },
			inverseJoinColumns = { @JoinColumn(name = "id_permission") }
			)
	private List<Permission> permissions = new ArrayList<Permission>();
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_at")
	private Date updateAt;

	@Override
	@Transient
	public String getAuthority() {
		return name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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
}
