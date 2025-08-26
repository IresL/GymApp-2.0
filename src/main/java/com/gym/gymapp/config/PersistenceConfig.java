package com.gym.gymapp.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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

    // ჭიქა წყალი პლეისჰოლდერებისთვის (Boot-ს გარეშე საჭიროა)
    @Bean
    public static org.springframework.context.support.PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new org.springframework.context.support.PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(Environment env) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("db.url", "jdbc:postgresql://localhost:5432/gymdb"));
        ds.setUsername(env.getProperty("db.username", "postgres"));
        ds.setPassword(env.getProperty("db.password", ""));
        ds.setDriverClassName(env.getProperty("db.driver", "org.postgresql.Driver"));
        // სურვილის მიხედვით მინიმალური ტიუნინგი
        ds.setMaximumPoolSize(Integer.parseInt(env.getProperty("db.pool.max", "10")));
        ds.setMinimumIdle(Integer.parseInt(env.getProperty("db.pool.min", "10")));
        return ds;
    }

    @Bean
    public Flyway flyway(DataSource dataSource, Environment env) {
        String locations = env.getProperty("flyway.locations", "classpath:db/migration");
        boolean enabled = Boolean.parseBoolean(env.getProperty("flyway.enabled", "true"));
        boolean baselineOnMigrate = Boolean.parseBoolean(env.getProperty("flyway.baselineOnMigrate", "true"));
        String baselineVersion = env.getProperty("flyway.baselineVersion", "1");
        String schema = env.getProperty("db.schema", "public");

        Flyway fw = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .schemas(schema)
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(MigrationVersion.fromVersion(baselineVersion))
                .load();

        if (enabled) {
            fw.migrate();
        }
        return fw;
    }

    @Bean
    @DependsOn("flyway") // ჯერ მიგრაციები, მერე EMF
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
        String packages = env.getProperty("jpa.packages", "com.gym.gymapp.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        // ეს უბრალოდ ლოგინგს ეხება; რეალურ SQL-ს Hibernate-ის props აკონტროლებს
        vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("hibernate.show_sql", "true")));
        vendorAdapter.setGenerateDdl(false);

        Properties jpaProps = new Properties();
        jpaProps.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "validate"));
        jpaProps.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql", "true"));
        jpaProps.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql", "true"));
        // სურვილისამებრ დიალექტი — Hibernate 6 ავტომატურადაც არჩევს, მაგრამ სურვილის შემთხვევაში წაიკითხავს:
        String dialect = env.getProperty("hibernate.dialect");
        if (dialect != null && !dialect.isBlank()) {
            jpaProps.setProperty("hibernate.dialect", dialect);
        }

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setPackagesToScan(packages);
        emf.setJpaProperties(jpaProps);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager tx = new JpaTransactionManager();
        tx.setEntityManagerFactory(emf);
        return tx;
    }

    // სტატიკურად გამოვაცხადოთ, რომ PostProcessor-ების გაფრთხილება აღარ მივიღოთ
    @Bean
    public static PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
