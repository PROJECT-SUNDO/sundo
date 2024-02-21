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


	@Configuration
	@Profile("dev1")
	static class DbConfigDev1 {
		@Bean
		public EntityManagerFactory entityManagerFactory() {
			return Persistence.createEntityManagerFactory("jpa_dev1");
		}

		@Bean
		@Primary
		public EntityManager entityManager() {
			return entityManagerFactory().createEntityManager();
		}

		@Bean
		public JpaTransactionManager transactionManager() {

			return new JpaTransactionManager(entityManagerFactory());
		}
	}

	@Configuration
	@Profile("dev2")
	static class DbConfigDev2 {
		@Bean
		public EntityManagerFactory entityManagerFactory() {
			return Persistence.createEntityManagerFactory("jpa_dev2");
		}

		@Bean
		@Primary
		public EntityManager entityManager() {
			return entityManagerFactory().createEntityManager();
		}

		@Bean
		public JpaTransactionManager transactionManager() {

			return new JpaTransactionManager(entityManagerFactory());
		}
	}

	@Configuration
	@Profile("prod")

	static class DbConfigProd {
		@Bean
		public EntityManagerFactory entityManagerFactory() {
			return Persistence.createEntityManagerFactory("jpa_prod");
		}

		@Bean
		@Primary
		public EntityManager entityManager() {
			return entityManagerFactory().createEntityManager();
		}

		@Bean
		public JpaTransactionManager transactionManager() {

			return new JpaTransactionManager(entityManagerFactory());
		}
	}




}
