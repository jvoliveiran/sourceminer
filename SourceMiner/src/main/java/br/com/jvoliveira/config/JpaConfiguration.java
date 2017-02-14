/**
 * 
 */
package br.com.jvoliveira.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author João Victor
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "br.com.jvoliveira.sourceminer.repository",
entityManagerFactoryRef = "entityManagerFactory",
transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class JpaConfiguration {

	@Autowired
    private Environment environment;
 
    @Value("${datasource.sampleapp.maxPoolSize:10}")
    private int maxPoolSize;
 
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.db.pgsql")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource() {
    	DataSourceProperties dataSourceProperties = dataSourceProperties();
    	return DataSourceBuilder.create(dataSourceProperties.getClassLoader())
    			 .driverClassName(dataSourceProperties.getDriverClassName())
                 .url(dataSourceProperties.getUrl())
                 .username(dataSourceProperties.getUsername())
                 .password(dataSourceProperties.getPassword())
                 .build();
    }
    
    /*
     * Entity Manager Factory setup.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(new String[] { "br.com.jvoliveira.sourceminer.domain" });
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }
 
    /*
     * Provider specific adapter.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        return hibernateJpaVendorAdapter;
    }
    
    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("spring.jpa.show-sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("spring.jpa.format-sql"));
        return properties;
    }
 
    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }
}
