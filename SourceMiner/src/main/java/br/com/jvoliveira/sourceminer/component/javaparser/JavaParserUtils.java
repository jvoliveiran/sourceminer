/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.Arrays;
import java.util.List;

/**
 * @author João Victor
 *
 */
public class JavaParserUtils {

	private static List<String> javaClassType = Arrays.asList("String", "Integer", "Double", "Boolean");
	
	public static boolean isJavaClassType(String classType){
		return javaClassType.contains(classType);
	}
}
