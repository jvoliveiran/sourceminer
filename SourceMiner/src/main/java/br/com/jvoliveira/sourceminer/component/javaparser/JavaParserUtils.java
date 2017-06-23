/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.Arrays;
import java.util.List;

import br.com.jvoliveira.arq.utils.StringUtils;

/**
 * @author João Victor
 *
 */
public class JavaParserUtils {

	private static List<String> javaClassType = Arrays.asList("String", "Integer", "Double", "Boolean", "Thread", "Calendar");
	private static List<String> javaDataStructure = Arrays.asList("Map","HashMap","List","LinkedList","ArrayList","Iterator");
	
	public static boolean isCustomClassType(String classType){
		return !isJavaClassType(classType) && !isJavaDataStructure(classType);
	}
	
	public static boolean isJavaClassType(String classType){
		return javaClassType.contains(classType);
	}
	
	public static boolean isJavaStaticCall(String expression){
		return !expression.contains(".") 
				&& StringUtils.isFirstUpperCase(expression) 
				&& !isJavaClassType(expression)
				&& !isJavaDataStructure(expression);
	}
	
	public static boolean isJavaDataStructure(String classType){
		for(String javaStructure : javaDataStructure){
			if(classType.contains(javaStructure+"<"))
				return true;
		}
		return false;
	}
}
