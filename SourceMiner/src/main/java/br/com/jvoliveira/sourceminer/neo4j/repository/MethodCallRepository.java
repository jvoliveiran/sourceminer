/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;

/**
 * @author João Victor
 *
 */
public interface MethodCallRepository extends Neo4jRepository<MethodCall, Long>{

	@Query("MATCH (n:ClassNode)-[k]->(m:ClassNode) WHERE n.repositoryItemId = :idItem RETURN k")
	List<MethodCall> findAllMethodsCallFromNode(@Param("idItem") Long idItem);
	
}
