/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import br.com.jvoliveira.arq.utils.StringUtils;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.enums.CommitType;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionSVN implements RepositoryConnection{

	private RepositoryConnector connector;
	private SVNRepository repository;
	
	private RepositoryParseSVN parse = new RepositoryParseSVN();
	
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
	public RepositoryParse getParse() {
		return this.parse;
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
			//TODO: Tratar exceção
			e.printStackTrace();
			System.err.println("Não foi possível estabelecer conexão com o repositório");
		}
	}

	@Override
	public List<RepositoryItem> getAllProjectItens(Project project) {
		String path = project.getPath();
		List<RepositoryItem> repositoryItens = new ArrayList<RepositoryItem>();
		
		listEntries(path,repositoryItens);
		
		return repositoryItens;
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
					RepositoryItem repositoryItem = parse.parseDirEntryToRepositoryItem(entry, path);
					if(!listEntries.contains(repositoryItem))
						listEntries.add(repositoryItem);
				}
				
				
			}
		} catch (SVNException e) {
			e.printStackTrace();
			System.err.println("Não foi possível acessar o caminho especificado");
		}
	}
	
	@Override
	public List<RepositoryRevisionItem> getRevisionItensInProjectRange(Project project, Integer startRevision, Integer endRevision){
		List<RepositoryRevisionItem> revisionItemLogs = new ArrayList<RepositoryRevisionItem>();
		
		Collection<SVNLogEntry> entries = getSVNLogEntryByRevision(project.getPath(), startRevision, endRevision);
		
		Iterator<SVNLogEntry> iteratorEntries = entries.iterator();
		
		while(iteratorEntries.hasNext()){
			SVNLogEntry revisionLog = iteratorEntries.next();
			if ( revisionLog.getChangedPaths( ).size( ) > 0 ) {
				Set<String> changedPathsSet = revisionLog.getChangedPaths( ).keySet( );
				
				 for ( Iterator changedPaths = changedPathsSet.iterator( ); changedPaths.hasNext( ); ) {
					 SVNLogEntryPath entryPath = ( SVNLogEntryPath ) revisionLog.getChangedPaths( ).get( changedPaths.next( ) );
					 
					 if(isNewJavaFile(entryPath) || isUpdateJavaFile(entryPath)){
						 RepositoryRevisionItem revisionItemLog = new RepositoryRevisionItem();
						 
						 revisionItemLog.setRepositoryRevision(parse.parseToRepositoryRevision(revisionLog));
						 
	                	 RepositoryItem item = parse.parseToRepositoryItem(entryPath);
	                	 revisionItemLog.setRepositoryItem(item);
	                	 
	                	 if(entryPath.getType() == 'A')
	                		 revisionItemLog.setCommitType(CommitType.ADD);
	                	 else if(entryPath.getType() == 'M')
	                		 revisionItemLog.setCommitType(CommitType.MOD);
	                	 
	                	 revisionItemLogs.add(revisionItemLog);
	                 }
				 }
			}
		}
		
		return revisionItemLogs;
	}
	
	private boolean isNewJavaFile(SVNLogEntryPath entryPath){
		return entryPath.getKind() == SVNNodeKind.FILE 
       		 && entryPath.getPath().contains(".java")
       		 && entryPath.getType() == 'A';
	}
	
	private boolean isUpdateJavaFile(SVNLogEntryPath entryPath){
		return entryPath.getKind() == SVNNodeKind.FILE 
	       		 && entryPath.getPath().contains(".java")
	       		 && entryPath.getType() == 'M';
	}
	
	@Override
	public List<RepositoryRevision> getAllProjectRevision(Project project) {
		return listRevisions(project.getPath(), 1, -1);
	}
	
	@Override
	public List<RepositoryRevision> getRevisionsInRange(Project project, Integer start, Integer end) {
		return listRevisions(project.getPath(), start, end);
	}
	
	@Override
	public Long getLastRevisionNumber(Project project){
		List<RepositoryRevision> repositoryRevision = getRevisionsInRange(project,-1,-1);
		Long lastRevision = repositoryRevision.get(0).getRevision();
		return lastRevision;
	}
	
	private List<RepositoryRevision> listRevisions(String path, Integer startRevision, Integer endRevision) {
		List<RepositoryRevision> repositoryRevisions = new ArrayList<RepositoryRevision>();
		Collection<SVNLogEntry> revisions = getSVNLogEntryByRevision(path, startRevision, endRevision);
		
		Iterator<SVNLogEntry> iteratorRevisions = revisions.iterator();
		
		while(iteratorRevisions.hasNext()){
			SVNLogEntry revision = iteratorRevisions.next();
			RepositoryRevision repositoryRevision = parse.parseToRepositoryRevision(revision);
			repositoryRevisions.add(repositoryRevision);
		}
		
		return repositoryRevisions;
	}

	private Collection<SVNLogEntry> getSVNLogEntryByRevision(String path, Integer startRevision,
			Integer endRevision) {
		Collection<SVNLogEntry> revisions = new ArrayList<SVNLogEntry>();
		try {
			revisions = repository.log(new String[]{path}, null, startRevision, endRevision, true, true);
			
			Iterator<SVNLogEntry> iteratorRevisions = revisions.iterator();
			while(iteratorRevisions.hasNext()){
				SVNLogEntry revision = iteratorRevisions.next();
				
				if(!revisions.contains(revision))
					revisions.add(revision);
			}
			
		} catch (SVNException e) {
			e.printStackTrace();
			System.err.println("Não foi possível recuperar as revisões no repositório");
		}
		
		return revisions;
	}

	@Override
	public String getFileContent(String path, Long revision) {
		try {
			SVNNodeKind nodeKind = repository.checkPath(path, revision);
			
			if(nodeKind != SVNNodeKind.FILE){
				System.err.println("O caminho indicado não se refere a um arquivo");
				throw new RuntimeException("O caminho indicado não se refere a um arquivo");
			}else{
				SVNProperties svnProperties = new SVNProperties();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				repository.getFile(path, revision, svnProperties, baos);
				
				return StringUtils.getFromCharBuffer(baos);
			}
			
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public List<RepositoryItem> getItensInRevision(Project project,
			Integer startRevision, Integer endRevision) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
