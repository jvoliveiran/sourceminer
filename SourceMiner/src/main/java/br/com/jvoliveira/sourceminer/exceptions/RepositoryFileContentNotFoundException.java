/**
 * 
 */
package br.com.jvoliveira.sourceminer.exceptions;

/**
 * @author João Victor
 *
 */
public class RepositoryFileContentNotFoundException extends Exception{

	public RepositoryFileContentNotFoundException(String path, Long revision){
		super(generateMessage(path,revision));
	}

	private static String generateMessage(String path, Long revision) {
		return "A revisão " + revision 
				+ " não faz referencia a um arquivo no caminho " + path;
	}
	
}
