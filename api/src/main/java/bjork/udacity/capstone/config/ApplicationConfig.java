package bjork.udacity.capstone.config;

import bjork.udacity.capstone.repository.TestRepository;
import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = TestRepository.class)
@EnableTransactionManagement
class ApplicationConfig {

  @Value("${PG_URL}")
  private String pgUrl;

  @Value("${PG_USERNAME}")
  private String pgUsername;

  @Value("${PG_PASSWORD}")
  private String pgPassword;

  /*@Bean
  public DataSource dataSource() {

    return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(pgUrl)
            .username(pgUsername)
            .password(pgPassword)
            .build();
//    return builder.setType(EmbeddedDatabaseType.HSQL).build();
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.getJpaPropertyMap().put("hibernate.dialect", PostgreSQL10Dialect.class.getCanonicalName());
    vendorAdapter.getJpaDialect();

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("bjork.udacity.capstone.domain");
    factory.setDataSource(dataSource());
    factory.afterPropertiesSet();

    return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {

    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory());
    return txManager;
  }*/
}
