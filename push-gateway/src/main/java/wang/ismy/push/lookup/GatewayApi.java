package wang.ismy.push.lookup;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import wang.ismy.push.lookup.service.RedisService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MY
 * @date 2020/6/17 21:05
 */
@RestController
@Slf4j
@AllArgsConstructor
public class GatewayApi {

    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate;
    private RedisService redisService;

    @GetMapping
    public String getConnector(HttpServletRequest request) {
        int retries = 0;
        while (retries <= 5) {
            ServiceInstance service = loadBalancerClient.choose("connector-service");

            if (service == null) {
                log.info("客户 {} 无法获取 Connector", request.getRemoteHost());
                redisService.hashIncr("gateway-stat", "fail");
                return "";
            }

            try {
                String port = restTemplate.getForObject(service.getUri() + "/port", String.class);
                String connector = service.getHost() + ":" + port;
                log.info("客户 {} 经过 {} 次重试获取 Connector {}", request.getRemoteHost(), retries, connector);
                redisService.hashIncr("gateway-stat", "success");
                return connector;
            } catch (Exception e) {
                // 发生异常 既有可能是connector 挂了, 重试
                retries++;
            }
        }
        log.info("客户 {} 经过 {} 次重试无法获取 Connector", request.getRemoteHost(), retries);
        redisService.hashIncr("gateway-stat", "fail");
        return "";
    }


}
