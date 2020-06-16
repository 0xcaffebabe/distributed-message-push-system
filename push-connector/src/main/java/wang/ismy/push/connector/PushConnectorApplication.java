package wang.ismy.push.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author MY
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PushConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushConnectorApplication.class, args);
    }

}
