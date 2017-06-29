/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;

/**
 * @author João Victor
 *
 */
@Repository
public interface GraphRepository extends Neo4jRepository<ClassNode,	Long> {

	@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE a.name = {nodeName} RETURN a,r1,b,r2,c")
	Collection<ClassNode> findGenericDependecyGraphCallFrom(@Param("nodeName") String nodeName);
	
	//@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE a.name = {nodeName} AND a.projectId = {projectId} RETURN a,r1,b,r2,c")
	@Query("MATCH (c:ClassNode)-[r1:CALLS*0..2]->(b:ClassNode)-[r2:CALLS*0..2]->(a:ClassNode) WHERE b.name = {nodeName} AND b.projectId = {projectId} RETURN b,r1,c,r2,a")
	Collection<ClassNode> findDependecyGraphCallFrom(@Param("nodeName") String nodeName, @Param("projectId") Long projectId);
	
	@Query("MATCH (a:ClassNode)-[r1]->(b:ClassNode)-[r2]->(c:ClassNode) WHERE c.name = {nodeName} AND c.projectId = {projectId} RETURN a,r1,b,r2,c")
	Collection<ClassNode> findDependecyGraphCallTo(@Param("nodeName") String nodeName, @Param("projectId") Long projectId);
	
	@Query("MATCH result=(a:ClassNode {name: {nodeName}, projectId: {projectId} })-[r1:CALLS]->(b:ClassNode)-[r2:CALLS]->(c:ClassNode) WITH COLLECT(result) AS results CALL apoc.convert.toTree(results) yield value RETURN value")
	Map<String,Object> findJSONDependecyGraphCallFrom(@Param("nodeName") String nodeName, @Param("projectId") Long projectId);
	
	@Query("MATCH (a:ClassNode) OPTIONAL MATCH (a)-[r]->() WHERE a.projectId = {idProject} AND r.projectId = {idProject} DELETE a,r")
	void deleteDataFromProject(@Param("idProject") Integer idProject);
}
