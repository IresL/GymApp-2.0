package com.gym.gymapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:application.properties")
public class PersistenceConfig {

    // --- ეს static bean აუცილებელია, რომ @Value("${...}") იმუშაოს Core რეჟიმში
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // --- DB properties ---
    @Value("${db.url}")      private String url;
    @Value("${db.username}") private String username;
    @Value("${db.password}") private String password;
    @Value("${db.driver}")   private String driver;

    // --- Hibernate/JPA properties ---
    @Value("${hibernate.dialect}")      private String dialect;
    @Value("${hibernate.hbm2ddl.auto}") private String hbm2ddl;
    @Value("${hibernate.show_sql}")     private boolean showSql;
    @Value("${hibernate.format_sql}")   private boolean formatSql;

    @Value("${jpa.packages}") private String packagesToScan;

    // --- DataSource (HikariCP) ---
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

    // --- Flyway (მიგრაციები) ---
    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(DataSource ds,
                         Environment env,
                         @Value("${flyway.locations:classpath:db/migration}") String locations) {
        Flyway flyway = Flyway.configure()
                .locations(locations)
                .dataSource(ds)
                .load();
        boolean enabled = Boolean.parseBoolean(env.getProperty("flyway.enabled", "true"));
        if (enabled) {
            flyway.migrate();
        }
        return flyway;
    }

    // --- EntityManagerFactory ---
    @Bean
    @DependsOn("flyway") // ჯერ მიგრაციები, მერე EMF
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(packagesToScan);

        var vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(showSql);
        emf.setJpaVendorAdapter(vendor);

        Properties jpaProps = new Properties();
        jpaProps.setProperty("hibernate.dialect", dialect);
        jpaProps.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        jpaProps.setProperty("hibernate.format_sql", String.valueOf(formatSql));
        emf.setJpaProperties(jpaProps);

        return emf;
    }

    // --- Transaction Manager ---
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

    // --- Exception translation (JPA → Spring exceptions) ---
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
