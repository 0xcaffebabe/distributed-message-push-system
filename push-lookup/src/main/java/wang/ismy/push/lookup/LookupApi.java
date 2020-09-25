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
        ServiceInstance service = loadBalancerClient.choose("connector-service");
        if (service == null) {
            return "";
        }
        return service.getHost()+":"+restTemplate.getForObject(service.getUri()+"/port",String.class);
    }


}
