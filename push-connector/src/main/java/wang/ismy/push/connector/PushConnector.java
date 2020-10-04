package wang.ismy.push.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author MY
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class PushConnector {

    public static void main(String[] args) {
        SpringApplication.run(PushConnector.class, args);
    }

}
