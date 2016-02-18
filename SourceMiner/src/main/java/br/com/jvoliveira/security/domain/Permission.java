package br.com.jvoliveira.security.domain;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * Classe de domínio que representa as permissões de acesso
 * @author Joao Victor
 *
 */
@Entity
@Table(name = "permission")
public class Permission implements GrantedAuthority, ObjectDB{

	private static final long serialVersionUID = 0L;

	@Id
	@Column(name = "id_permission")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@Column(name = "name")
	private String name;
	
	@Column(name = "ativo")
	private Boolean ativo = true;

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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}
