package org.sundo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.sundo")
public class DbConfig {
	
	@Bean
	@Profile("dev1")
	public EntityManagerFactory entityManagerFactory() {
		return Persistence.createEntityManagerFactory("jpa_dev1");
	}
	@Bean
	@Profile("dev2")
	public EntityManagerFactory entityManagerFactory2() {
		return Persistence.createEntityManagerFactory("jpa_dev2"); 
	}
	@Bean
	@Profile("prod")
	public EntityManagerFactory entityManagerFactory3() {
		return Persistence.createEntityManagerFactory("jpa_prod");
	}
	
	
	@Bean
	@Primary
	@Profile("dev1")
	public EntityManager entityManagerDev1() {
		return entityManagerFactory().createEntityManager();
	}

	@Bean
	@Primary
	@Profile("dev2")
	public EntityManager entityManagerDev2() {
		return entityManagerFactory2().createEntityManager();
	}

	@Bean
	@Primary
	@Profile("prod")
	public EntityManager entityManagerProd() {
		return entityManagerFactory3().createEntityManager();
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		
		return new JpaTransactionManager(entityManagerFactory());
	}
}
