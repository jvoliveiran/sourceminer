/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.github.javaparser.ast.CompilationUnit;

import br.com.jvoliveira.sourceminer.component.javaparser.CompilationUnitHelper;

/**
 * @author João Victor
 *
 */
public class VisitorExecutor {
	
	private List<GenericClassVisitor> executeQueue;
	private Map<String,Object> resultMap;
	
	public VisitorExecutor(){
		executeQueue = new ArrayList<>();
		resultMap = new HashedMap<>();
	}
	
	public <T extends GenericClassVisitor> void addVisitor(T visitor){
		executeQueue.add(visitor);
	}

	public <T extends GenericClassVisitor> void addVisitorByClass(Class<T> visitorClass) throws InstantiationException, IllegalAccessException{
		executeQueue.add(visitorClass.newInstance());
	}
	
	public void executeVisitorChain(String fileContent, String filePath, Object arg){
		CompilationUnit compUnit = getCompilationUnit(fileContent, filePath);
		processQueue(compUnit, arg);
	}

	private void processQueue(CompilationUnit compUnit, Object arg){
		for(GenericClassVisitor visitor : executeQueue){
			visitor.init();
			visitor.execute(compUnit, arg, resultMap);
		}
	}
	
	private CompilationUnit getCompilationUnit(String fileContent, String filePath){
		return CompilationUnitHelper.getCompilationUnit(fileContent, filePath);
	}
	
	public Map<String, Object> getResultMap(){
		return this.resultMap;
	}
}	
