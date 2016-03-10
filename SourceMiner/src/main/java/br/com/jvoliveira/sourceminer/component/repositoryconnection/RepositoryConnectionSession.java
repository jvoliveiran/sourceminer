/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


/**
 * @author Joao Victor
 *
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RepositoryConnectionSession {

	private RepositoryConnection connection;

	public RepositoryConnection getConnection() {
		return connection;
	}

	public void setConnection(RepositoryConnection connection) {
		this.connection = connection;
	}
	
	public boolean isConnectionOpened(){
		return connection != null;
	}
	
	public String getConnectionName(){
		return this.connection.getConnector().getName();
	}
}
