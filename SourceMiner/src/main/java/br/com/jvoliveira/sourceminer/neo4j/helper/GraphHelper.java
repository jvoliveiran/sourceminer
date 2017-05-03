/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;

/**
 * @author João Victor
 *
 */
public class GraphHelper {

	public static Map<String,Object> convertCollectionToGraphMap(Collection<ClassNode> nodesFromGraph){
		Map<String,Object> jsonResult = new HashMap<>();
		List<Map<String,Object>> nodes = new ArrayList<>();
		List<Map<String,Object>> links = new ArrayList<>();
		
		List<MethodCall> methodsCalled = fillNodesList(nodes,nodesFromGraph);
		fillLinksList(links,nodes,methodsCalled);
		
		jsonResult.put("nodes", nodes);
		jsonResult.put("links", links);
		return jsonResult;
	}

	private static List<MethodCall> fillNodesList(List<Map<String, Object>> nodes, Collection<ClassNode> nodesFromGraph) {
		Iterator<ClassNode> nodesFromGraphIterator = nodesFromGraph.iterator();
		List<MethodCall> methodsCalled = new ArrayList<>();
		while(nodesFromGraphIterator.hasNext()){
			ClassNode node = nodesFromGraphIterator.next();
			Map<String,Object> nodeAsMap = new HashMap<>();
			nodeAsMap.put("id", node.getId());
			nodeAsMap.put("name", node.getName());
			nodes.add(nodeAsMap);
			methodsCalled.addAll(node.getMethodsCalled());
		}
		return methodsCalled;
	}

	private static void fillLinksList(List<Map<String, Object>> links, List<Map<String, Object>> nodes,
			List<MethodCall> methodsCalled) {
		Iterator<MethodCall> methodsCalledIterator = methodsCalled.iterator();
		while(methodsCalledIterator.hasNext()){
			MethodCall methodCalled = methodsCalledIterator.next();
			Map<String,Object> methodCallAsMap = new HashMap<>();
			methodCallAsMap.put("source", getIndexOfClassNodeInArray(methodCalled.getCaller(),nodes));
			methodCallAsMap.put("target", getIndexOfClassNodeInArray(methodCalled.getCalled(),nodes));
			links.add(methodCallAsMap);
		}
	}

	private static Integer getIndexOfClassNodeInArray(ClassNode node, List<Map<String, Object>> nodes) {
		int index = 0;
		for(Map<String,Object> nodeMap : nodes){
			if(nodeMap.get("id").equals(node.getId()))
				break;
			index++;
		}
		return index;
	}
	
}
