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
public class RepositoryRevisionFilter implements ArqFilter{
	
	private Boolean checkRevision;
	private String revision;
	
	private Boolean checkAuthor;
	private String author;
	
	private Boolean checkComment;
	private String comment;
	
	private Project project;

	@Override
	public boolean hasFilterToSearch() {
		
		if(isValidRevision())
			return true;
		
		if(isValidAuthor())
			return true;
		
		if(isValidComment())
			return true;
		
		return false;
	}
	
	public boolean isValidRevision(){
		if(checkRevision != null && checkRevision
				&& revision != null && !revision.equals(""))
			return true;
		return false;
	}
	
	public boolean isValidAuthor(){
		if(checkAuthor != null && checkAuthor
				&& author != null && !author.trim().equals(""))
			return true;
		return false;
	}
	
	public boolean isValidComment(){
		if(checkComment != null && checkComment
				&& comment != null && !comment.trim().equals(""))
			return true;
		return false;
	}

	public Boolean getCheckRevision() {
		return checkRevision;
	}

	public void setCheckRevision(Boolean checkRevision) {
		this.checkRevision = checkRevision;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Boolean getCheckAuthor() {
		return checkAuthor;
	}

	public void setCheckAuthor(Boolean checkAuthor) {
		this.checkAuthor = checkAuthor;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Boolean getCheckComment() {
		return checkComment;
	}

	public void setCheckComment(Boolean checkComment) {
		this.checkComment = checkComment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
