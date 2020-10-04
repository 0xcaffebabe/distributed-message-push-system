package wang.ismy.push.lookup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

/**
 * @author MY
 * @date 2020/6/17 21:05
 */
@RestController
@Slf4j
public class GatewayApi {

    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate;

    public GatewayApi(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getConnector(HttpServletRequest request) {
        int retries = 0;
        while (retries <= 5) {
            ServiceInstance service = loadBalancerClient.choose("connector-service");
            log.info("客户 {} 无法获取 Connector", request.getRemoteHost());
            if (service == null) { return ""; }

            try {
                String port = restTemplate.getForObject(service.getUri() + "/port", String.class);
                String connector = service.getHost() + ":" + port;
                log.info("客户 {} 经过 {} 次重试获取 Connector {}", request.getRemoteHost(), retries, connector);
                return connector;
            } catch (Exception e) {
                // 发生异常 既有可能是connector 挂了, 重试
                retries++;
            }
        }
        log.info("客户 {} 经过 {} 次重试无法获取 Connector", request.getRemoteHost(), retries);
        return "";
    }


}
