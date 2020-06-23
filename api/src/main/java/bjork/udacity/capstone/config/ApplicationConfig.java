package bjork.udacity.capstone.config;

import bjork.udacity.capstone.repository.TestRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = TestRepository.class)
@EnableTransactionManagement
class ApplicationConfig {

}
