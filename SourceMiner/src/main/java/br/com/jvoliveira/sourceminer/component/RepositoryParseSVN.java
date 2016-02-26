/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import br.com.jvoliveira.arq.utils.StringUtils;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author Joao Victor
 *
 */
public class RepositoryParseSVN implements RepositoryParse{

	@Override
	public <T> RepositoryItem parseToRepositoryItem(T entry) {
		SVNLogEntryPath logEntryPath = (SVNLogEntryPath) entry;
		
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(StringUtils.getFileNameInPath(logEntryPath.getPath(), "/"));
		repositoryItem.setPath(logEntryPath.getPath());
		repositoryItem.setExtension("java");
		
		return repositoryItem;
	}
	
	public RepositoryItem parseDirEntryToRepositoryItem(SVNDirEntry dirEntry, String path){
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(dirEntry.getName());
		repositoryItem.setPath(path);
		
		return repositoryItem;
	}

	@Override
	public <T> RepositoryRevision parseToRepositoryRevision(T entry) {
		SVNLogEntry logEntry = (SVNLogEntry) entry;
		RepositoryRevision repositoryRevision = new RepositoryRevision();
		
		repositoryRevision.setRevision(logEntry.getRevision());
		repositoryRevision.setDateRevision(logEntry.getDate());
		repositoryRevision.setComment(logEntry.getMessage());
		repositoryRevision.setAuthor(logEntry.getAuthor());
		
		return repositoryRevision;
	}

	@Override
	public <T> RepositoryRevisionItem parseToRepositoryRevisionItem(T entry) {
		RepositoryRevisionItem revisionItem = new RepositoryRevisionItem();
		
		//TODO
		
		return revisionItem;
	}

}
