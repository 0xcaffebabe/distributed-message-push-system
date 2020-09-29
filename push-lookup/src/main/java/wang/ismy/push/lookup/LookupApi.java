package wang.ismy.push.lookup;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

/**
 * @author MY
 * @date 2020/6/17 21:05
 */
@RestController
public class LookupApi {

    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate;

    public LookupApi(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getConnector(){
        int retries = 0;
        while (retries <= 5) {
            ServiceInstance service = loadBalancerClient.choose("connector-service");
            if (service == null) {
                return "";
            }

            try {
                 String port = restTemplate.getForObject(service.getUri()+"/port",String.class);
                return service.getHost()+":"+port;
            }catch (Exception e) {
                // 发生异常 既有可能是connector 挂了, 重试
                retries++;
            }
        }
        return "";
    }


}
