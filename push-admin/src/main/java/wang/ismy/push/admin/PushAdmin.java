package wang.ismy.push.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author MY
 */
@SpringBootApplication
public class PushAdmin {

    public static void main(String[] args) {
        SpringApplication.run(PushAdmin.class, args);
    }

    @Bean
    public MessageConfirmListener messageConfirmListener() {
        return new MessageConfirmListener();
    }
}
