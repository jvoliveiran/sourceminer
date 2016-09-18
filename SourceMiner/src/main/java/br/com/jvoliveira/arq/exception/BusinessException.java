/**
 * 
 */
package br.com.jvoliveira.arq.exception;

/**
 * @author João Victor
 *
 */
public class BusinessException extends Exception{
	
	public BusinessException(){
		super();
	}
	
	public BusinessException(String message){
		super(message);
	}

}
