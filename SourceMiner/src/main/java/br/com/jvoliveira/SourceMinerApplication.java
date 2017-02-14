package br.com.jvoliveira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) 
@EnableAsync
public class SourceMinerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SourceMinerApplication.class, args);
	}
}
