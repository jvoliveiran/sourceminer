/**
 * 
 */
package br.com.jvoliveira.sourceminer.search.filter;

import br.com.jvoliveira.arq.search.ArqFilter;
import br.com.jvoliveira.sourceminer.domain.Project;

/**
 * @author Joao Victor
 *
 */
public class RepositoryItemFilter implements ArqFilter{

	private Boolean checkName;
	private String name;
	
	private Project project;
	
	public Boolean getCheckName() {
		return checkName;
	}

	public void setCheckName(Boolean checkName) {
		this.checkName = checkName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isValidName(){
		return checkName != null 
				&& checkName
				&& name != null
				&& !name.trim().equals("");
	}

	@Override
	public boolean hasFilterToSearch() {
		
		return isValidName();
		
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
