/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;

/**
 * @author João Victor
 *
 */
public interface ClassNodeRepository extends GraphRepository<ClassNode>{

	@Query("MATCH (n:ClassNode) WHERE n.repositoryItemId IN {ids} RETURN n")
	List<ClassNode> findAllNodesOfItems(@Param("ids") List<Long> idsItems);
	
	@Query("MATCH (n:ClassNode)-[k:CALLS]->(m:ClassNode) WHERE n.repositoryItemId = {idItem} RETURN n as graphNode, k as methodCall, m as graphNodeDestiny")
	List<MethodCall> getAllMethodsCallFromNode(@Param("idItem") Long idItem);
}
