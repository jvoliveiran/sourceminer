/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;

/**
 * @author João Victor
 *
 */
public interface ClassNodeRepository extends Neo4jRepository<ClassNode, Long>{

	@Query("MATCH (n:ClassNode) WHERE n.repositoryItemId IN [:ids] RETURN n")
	List<ClassNode> findAllNodesOfItems(@Param("ids") List<Long> idsItems);
	
}
