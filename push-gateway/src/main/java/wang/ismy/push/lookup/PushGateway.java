package wang.ismy.push.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author MY
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PushGateway {

    public static void main(String[] args) {
        SpringApplication.run(PushGateway.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
