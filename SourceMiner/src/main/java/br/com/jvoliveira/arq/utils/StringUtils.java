/**
 * 
 */
package br.com.jvoliveira.arq.utils;

/**
 * @author Joao Victor
 *
 */
public class StringUtils {

	public static String getFileNameInPath(String path, String separator){
		
		Integer lastIndex = path.lastIndexOf(separator);
		
		String name = path.substring(lastIndex+1, path.length());
		
		return name;
	}
	
}
