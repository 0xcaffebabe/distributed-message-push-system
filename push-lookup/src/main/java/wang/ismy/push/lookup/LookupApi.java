package wang.ismy.push.lookup;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

    private DiscoveryClient discoveryClient;
    private RestTemplate restTemplate;

    public LookupApi(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getConnector(){
        List<ServiceInstance> connector = discoveryClient.getInstances("connector-service");
        Random random = new Random();
        int i = random.nextInt(connector.size());
        ServiceInstance service = connector.get(i);
        return service.getHost()+":"+restTemplate.getForObject(service.getUri()+"/port",String.class);
    }


}
