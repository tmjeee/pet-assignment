package tmjee.pet.config;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tmjee.pet.commons.DbService;
import tmjee.pet.commons.DogCeoService;
import tmjee.pet.commons.S3BucketService;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableConfigurationProperties({PetAppConfigProperties.class})
public class PetAppConfig implements WebMvcConfigurer {

    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy hh:mm:ss";

    @Autowired
    private DataSource dataSource;

    @Bean
    public Jdbi jdbi() {
        return Jdbi.create(dataSource);
    }

    @Bean
    public DbService dbService() {
        return new DbService();
    }

    @Bean
    public S3BucketService s3BucketService() {
        return new S3BucketService();
    }

    @Bean
    public DogCeoService downloadService() {
        return new DogCeoService();
    }

}
