/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.helper.GraphHelper;
import br.com.jvoliveira.sourceminer.neo4j.repository.GraphRepository;

/**
 * @author João Victor
 *
 */
@RestController
@RequestMapping("/graph")
public class GraphRestController {

	private GraphRepository repository;
	
	@Autowired
	public GraphRestController(GraphRepository repository){
		this.repository = repository;
	}
	
	@ResponseBody
	@RequestMapping("/findJSONDependecyGraphCallFrom")
	public Map<String,Object> getJSONGraph(@RequestParam("nodeName") String nodeName, @RequestParam("projectId") Long projectId){
		return repository.findJSONDependecyGraphCallFrom(nodeName, projectId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findPlainJSONDependecyGraphCallFrom", produces = "application/json")
	public String getPlainJSONGraph(@RequestParam("nodeName") String nodeName, @RequestParam("projectId") Long projectId) throws JsonProcessingException{
		Collection<ClassNode> result = repository.findDependecyGraphCallFrom(nodeName, projectId);
		Map<String,Object> resultAsMap = GraphHelper.convertCollectionToGraphMap(result);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(resultAsMap);
	}
}
