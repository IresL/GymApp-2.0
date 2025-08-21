package com.gym.gymapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gym.gymapp.repository")
@ComponentScan(basePackages = "com.gym.gymapp")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {

    // --- Spring Boot–style property keys (kept, even in Core setup) ---
    @Value("${spring.datasource.url}") private String url;
    @Value("${spring.datasource.username}") private String username;
    @Value("${spring.datasource.password}") private String password;
    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}") private String driver;

    @Value("${spring.jpa.properties.hibernate.dialect:org.hibernate.dialect.PostgreSQLDialect}")
    private String dialect;
    @Value("${spring.jpa.hibernate.ddl-auto:validate}") private String hbm2ddl;
    @Value("${spring.jpa.show-sql:true}") private boolean showSql;
    @Value("${spring.jpa.properties.hibernate.format_sql:true}") private boolean formatSql;

    @Value("${spring.flyway.locations:classpath:db/migration}") private String flywayLocations;

    private static final String PACKAGES_TO_SCAN = "com.gym.gymapp.model";

    // --- DataSource (Hikari) ---
    @Bean
    public DataSource dataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName(driver);
        cfg.setMaximumPoolSize(10);
        return new HikariDataSource(cfg);
    }

    // --- JPA / Hibernate EMF ---
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(PACKAGES_TO_SCAN);

        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(showSql);
        emf.setJpaVendorAdapter(vendor);

        Properties jpaProps = new Properties();
        jpaProps.setProperty("hibernate.dialect", dialect);
        jpaProps.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        jpaProps.setProperty("hibernate.format_sql", String.valueOf(formatSql));
        emf.setJpaProperties(jpaProps);

        return emf;
    }

    // --- Tx Manager ---
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

    // --- Translate JPA exceptions to Spring DataAccessException hierarchy ---
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    // --- Flyway (conditional by spring.flyway.enabled) ---
    @Bean(initMethod = "migrate")
    @DependsOn("dataSource")
    @Conditional(FlywayEnabled.class)
    public Flyway flyway(DataSource ds) {
        return Flyway.configure()
                .locations(flywayLocations)
                .dataSource(ds)
                .load();
    }

    /** Enables Flyway bean only if spring.flyway.enabled=true (default true). */
    public static class FlywayEnabled implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String enabled = context.getEnvironment().getProperty("spring.flyway.enabled", "true");
            return Boolean.parseBoolean(enabled);
        }
    }
}
