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
	
	@Query("MATCH (n2)-[k2*0..2]->(n:ClassNode)-[k]->(m:ClassNode) WHERE k.itemAssetId = {itemAssetId} RETURN n, n2 LIMIT 300")
	List<ClassNode> getNodesCallingMethod(@Param("itemAssetId") Long itemAssetId);
	
	@Query("MATCH (c:ClassNode)-[k1:ACCESS]-(b:ClassNode)-[k2:ACCESS]-(a:ClassNode)<-[k3:CALLS*0..2]-(x:ClassNode) WHERE k1.methodId = {itemAssetId} AND k1.fieldId = k2.fieldId RETURN a,x LIMIT 300")
	List<ClassNode> getNodesCallingMethodThroughtField(@Param("itemAssetId") Long itemAssetId);
}
