/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionSVN implements RepositoryConnection{

	private RepositoryConnector connector;
	private SVNRepository repository;
	
	public RepositoryConnectionSVN(){
		
	}
	
	public RepositoryConnectionSVN(RepositoryConnector connector){
		this.connector = connector;
	}
	
	@Override
	public RepositoryConnector getConnector() {
		return this.connector;
	}

	@Override
	public void setConnector(RepositoryConnector connector) {
		this.connector = connector;
	}
	
	@Override
	public void openConnection() {
		try {
			if(connector.getLocation().isLocal())
				FSRepositoryFactory.setup();
			else if(connector.getLocation().isRemote())
				DAVRepositoryFactory.setup();
			
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(this.connector.getUrl()));
			ISVNAuthenticationManager authManager;
			if(connector.isAnonymousConnection())
				authManager = SVNWCUtil.createDefaultAuthenticationManager();
			else
				authManager = SVNWCUtil.createDefaultAuthenticationManager(connector.getUsername(), connector.getPassword().toCharArray());
			
			repository.setAuthenticationManager(authManager);
			
		} catch (SVNException e) {
			e.printStackTrace();
			System.err.println("Não foi possível estabelecer conexão com o repositório");
		}
	}
	
	private void listEntries(String path, List<RepositoryItem> listEntries) {
		Collection<SVNDirEntry> entries;
		try {
			entries = repository.getDir( path, -1 , null , (Collection<SVNDirEntry>) null );
			
			Iterator<SVNDirEntry> iterator = entries.iterator( );
			
			while ( iterator.hasNext( ) ) {
				SVNDirEntry entry = iterator.next( );
				
				if ( entry.getKind() == SVNNodeKind.DIR ) {
					listEntries( ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ), listEntries );
				}else if(entry.getKind() == SVNNodeKind.FILE && entry.getName().contains(".java")){
					RepositoryItem repositoryItem = parseDirEntryToRepositoryItem(entry, path);
					if(!listEntries.contains(repositoryItem))
						listEntries.add(repositoryItem);
				}
				
				
			}
		} catch (SVNException e) {
			e.printStackTrace();
			System.err.println("Não foi possível acessar o caminho especificado");
		}
	}
	
	private RepositoryItem parseDirEntryToRepositoryItem(SVNDirEntry dirEntry, String path){
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(dirEntry.getName());
		repositoryItem.setPath(path);
		
		return repositoryItem;
	}
	
	private RepositoryRevision parseLogEntryToRepositoryRevision(SVNLogEntry logEntry){
		RepositoryRevision repositoryRevision = new RepositoryRevision();
		
		repositoryRevision.setRevision(logEntry.getRevision());
		repositoryRevision.setDateRevision(logEntry.getDate());
		repositoryRevision.setComment(logEntry.getMessage());
		repositoryRevision.setAuthor(logEntry.getAuthor());
		
		return repositoryRevision;
	}
	
	@Override
	public List<RepositoryItem> getAllProjectItens(Project project) {
		String path = project.getPath();
		List<RepositoryItem> repositoryItens = new ArrayList<RepositoryItem>();
		
		listEntries(path,repositoryItens);
		
		return repositoryItens;
	}
	
	private List<RepositoryRevision> listRevisions(String path, Integer startRevision, Integer endRevision) {
		Collection<SVNLogEntry> revisions = null;
		List<RepositoryRevision> repositoryRevisions = new ArrayList<RepositoryRevision>();
		try {
			revisions = repository.log(new String[]{path}, null, startRevision, endRevision, true, true);
			
			Iterator<SVNLogEntry> iteratorRevisions = revisions.iterator();
			while(iteratorRevisions.hasNext()){
				SVNLogEntry revision = iteratorRevisions.next();
				
				RepositoryRevision repositoryRevision = parseLogEntryToRepositoryRevision(revision);
				repositoryRevisions.add(repositoryRevision);
			}
			
		} catch (SVNException e) {
			e.printStackTrace();
			System.err.println("Não foi possível recuperar as revisões no repositório");
		}
		
		return repositoryRevisions;
	}
	
	@Override
	public List<RepositoryRevision> getAllProjectRevision(Project project) {
		return listRevisions(project.getPath(), 1, -1);
	}	
	
	@Override
	public void closeConnection() {
		if(this.repository != null)
			this.repository.closeSession();
		
		this.repository = null;
	}

	@Override
	public Boolean isConnectionOpened() {
		return this.repository != null;
	}
	
	@Override
	public Boolean isGit() {
		return false;
	}

	@Override
	public Boolean isSVN() {
		return true;
	}

	@Override
	public Boolean isCVS() {
		return false;
	}
	
}
