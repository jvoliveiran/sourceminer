/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;

/**
 * @author João Victor
 *
 */
public interface MethodCallRepository extends Neo4jRepository<MethodCall, Long>{

}
