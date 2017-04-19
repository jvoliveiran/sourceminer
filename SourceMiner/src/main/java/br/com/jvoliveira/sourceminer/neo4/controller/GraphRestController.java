/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
