/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

/**
 * @author João Victor
 *
 */
public class CallGraphVisitorExecutor {

	private static VisitorExecutor visitorExecutor;
	
	private static void init(){
		try {
			visitorExecutor = new VisitorExecutor();
			visitorExecutor.addVisitorByClass(MethodCallVisitor.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void processCallGraph(String fileContent, String filePath, Object arg){
		init();
		visitorExecutor.executeVisitorChain(fileContent, filePath, arg);
	}
	
}
