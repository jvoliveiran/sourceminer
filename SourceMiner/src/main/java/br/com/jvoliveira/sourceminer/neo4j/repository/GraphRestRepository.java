/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;

/**
 * Exemplo de chamada por url: http://localhost:8081/SourceMiner/graph/search/findDependecyGraphCallFrom?nodeName=ProjectController.java&projectId=2
 * @author João Victor
 *
 */
@RepositoryRestResource(collectionResourceRel = "classNode", path = "graph")
public interface GraphRestRepository extends Neo4jRepository<ClassNode, Long>{

	@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE a.name = {nodeName} RETURN a,r1,b,r2,c")
	Collection<ClassNode> findGenericDependecyGraphCallFrom(@Param("nodeName") String nodeName);
	
	@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE a.name = {nodeName} AND a.projectId = {projectId} RETURN a,r1,b,r2,c")
	Collection<ClassNode> findDependecyGraphCallFrom(@Param("nodeName") String nodeName, @Param("projectId") Long projectId);
	
	@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE c.name = {nodeName} AND c.projectId = {projectId} RETURN a,r1,b,r2,c")
	Collection<ClassNode> findDependecyGraphCallTo(@Param("nodeName") String nodeName, @Param("projectId") Long projectId);
	
}
