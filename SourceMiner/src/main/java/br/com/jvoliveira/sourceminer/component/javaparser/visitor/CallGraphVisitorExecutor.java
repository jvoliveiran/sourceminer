/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.jvoliveira.sourceminer.component.javaparser.JavaParserUtils;

/**
 * @author João Victor
 *
 */
public class CallGraphVisitorExecutor {

	private static VisitorExecutor visitorExecutor;
	private static Map<String,String> fields;
	private static Map<String,String> variables;
	private static Map<String,String> externalConstants;
	private static Map<String,String> methodsASvariables;
	
	private static void init(){
		try {
			visitorExecutor = new VisitorExecutor();
			visitorExecutor.addVisitorByClass(FieldDeclarationVisitor.class);
			visitorExecutor.addVisitorByClass(VariableDeclaratorVisitor.class);
			visitorExecutor.addVisitorByClass(MethodDeclaratorVisitor.class);
			visitorExecutor.addVisitorByClass(FieldAccessVisitor.class);
			visitorExecutor.addVisitorByClass(MethodCallVisitor.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,Collection<GraphElementDTO>> processCallGraph(String fileContent, String filePath){
		return processCallGraph(fileContent, filePath,null);
	}
	
	public static Map<String,Collection<GraphElementDTO>> processCallGraph(String fileContent, String filePath, Object arg){
		init();
		visitorExecutor.executeVisitorChain(fileContent, filePath, arg);
		return processOutput();
	}
	
	private static Map<String,Collection<GraphElementDTO>> processOutput() {
		initMappers();
		Map<String,Collection<GraphElementDTO>> output = new HashMap<>();
		includeFieldVariables();
		includeTempVariables();
		includeClassMethodsASVariables();
		includeExternalConstants(output);
		processMethodCall(output);
		return output;
	}	

	private static void initMappers() {
		fields = new HashMap<>();
		variables = new HashMap<>();
		methodsASvariables = new HashMap<>();
		externalConstants = new HashMap<>();
	}

	private static void includeClassMethodsASVariables() {
		String prefixKeys = "ClassMethodDeclaration_";
		extractVariablesFromResultMap(prefixKeys,methodsASvariables);
	}
	
	private static void includeFieldVariables() {
		String prefixKeys = "FieldDeclaration_";
		extractVariablesFromResultMap(prefixKeys,fields);
	}

	private static void includeTempVariables() {
		String prefixKeys = "VariableDeclaration_";
		extractVariablesFromResultMap(prefixKeys,variables);
	}
	

	private static void includeExternalConstants(Map<String,Collection<GraphElementDTO>> output) {
		String prefixKeys = "FieldAccessExpr_";
		extractVariablesFromResultMap(prefixKeys, externalConstants);
		Iterator<String> externalConstIterator = externalConstants.keySet().iterator();
		while(externalConstIterator.hasNext()){
			String key = externalConstIterator.next();
			String className = externalConstants.get(key);
		
			if(output.get(className) == null)
				output.put(className, new ArrayList<>());
			
			output.get(className).add(new GraphElementDTO(key, GraphElementType.ACCESS_FIELD));
		}
	}

	
	private static void processMethodCall(Map<String,Collection<GraphElementDTO>> output) {
		Map<String,Object> visitorResult = visitorExecutor.getResultMap();
		Iterator<String> keySet = visitorResult.keySet().iterator();
		String prefixKey = "MethodCallExpr_";
		while(keySet.hasNext()){
			String key = keySet.next();
			if(key != null && key.contains(prefixKey) && visitorResult.get(key) != null){
				String methodName = removeKeyPrefix(key);
				String variableName = visitorResult.get(key).toString();
				
				String className = variables.get(variableName.replaceAll("this.", ""));
				if(className == null)
					className = fields.get(variableName.replaceAll("this.", ""));
				if(className == null)
					className = methodsASvariables.get(variableName.replaceAll("this.", "").replace("()",""));
				if(className == null && JavaParserUtils.isJavaStaticCall(variableName))
					className = variableName;
				if(className == null)
					continue;
				
				if(output.get(className) == null)
					output.put(className, new ArrayList<>());
				
				output.get(className).add(new GraphElementDTO(methodName, GraphElementType.METHOD_CALL));
			}
		}
	}
	
	private static void extractVariablesFromResultMap(String prefixKey, Map<String,String> mapOfInstances){
		Map<String,Object> visitorResult = visitorExecutor.getResultMap();
		Iterator<String> keySet = visitorResult.keySet().iterator();
		while(keySet.hasNext()){
			String key = keySet.next();
			if(key.contains(prefixKey)){
				mapOfInstances.put(removeKeyPrefix(key), visitorResult.get(key).toString());
			}
		}
	}
	
	private static String removeKeyPrefix(String key) {
		int indexOfSeparator = key.indexOf("_");
		return key.substring(indexOfSeparator+1, key.length());
	}

	public static Map<String,Object> getMapResult(){
		return visitorExecutor.getResultMap();
	}
}
