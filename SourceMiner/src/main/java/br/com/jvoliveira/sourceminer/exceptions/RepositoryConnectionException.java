package br.com.jvoliveira.sourceminer.exceptions;

/**
 * 
 * @author João Victor
 *
 */
public class RepositoryConnectionException extends Exception{

	public RepositoryConnectionException(){
		super();
	}
	
	public RepositoryConnectionException(String message){
		super(message);
	}
	
}
