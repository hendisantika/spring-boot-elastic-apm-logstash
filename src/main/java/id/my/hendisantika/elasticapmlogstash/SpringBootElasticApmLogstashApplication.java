package id.my.hendisantika.elasticapmlogstash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class SpringBootElasticApmLogstashApplication {

    static void main(String[] args) {
        SpringApplication.run(SpringBootElasticApmLogstashApplication.class, args);
    }

}
