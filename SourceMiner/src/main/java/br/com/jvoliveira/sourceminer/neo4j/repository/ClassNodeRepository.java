/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;

/**
 * @author João Victor
 *
 */
public interface ClassNodeRepository extends Neo4jRepository<ClassNode, Long>{

}
