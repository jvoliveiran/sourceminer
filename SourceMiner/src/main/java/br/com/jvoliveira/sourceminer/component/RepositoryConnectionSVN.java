/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;

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
