package wang.ismy.push.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MY
 * @date 2020/6/17 20:41
 */
@RestController
public class Api {

    @Value("${connector.port}")
    private int port;

    @GetMapping("port")
    public int getConnectorPort(){
        return port;
    }
}
