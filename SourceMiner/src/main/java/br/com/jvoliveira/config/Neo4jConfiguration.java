package br.com.jvoliveira.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author João Victor
 *
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "br.com.jvoliveira.sourceminer.neo4j.repository")
@EnableTransactionManagement
public class Neo4jConfiguration{

	@Bean
	public org.neo4j.ogm.config.Configuration configuration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
		config.driverConfiguration()
				.setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
				.setURI("bolt://neo4j:admin@localhost");
		return config;
	}

	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory(configuration(),"br.com.jvoliveira.sourceminer.neo4j.domain");
	}

	@Bean
	public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(sessionFactory());
	}
}
