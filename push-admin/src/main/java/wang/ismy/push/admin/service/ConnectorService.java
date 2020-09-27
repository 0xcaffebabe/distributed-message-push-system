package wang.ismy.push.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import wang.ismy.push.admin.entity.ConnectorDTO;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MY
 * @date 2020/9/27 21:37
 */
@Service
@AllArgsConstructor
public class ConnectorService {

    private final DiscoveryClient discoveryClient;

    public List<ConnectorDTO> getConnectorList() throws IOException {
        List<ServiceInstance> instances = discoveryClient.getInstances("connector-service");
        List<ConnectorDTO> connectors = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            ConnectorDTO connector = generateConnector(instance);
            connectors.add(connector);
        }
        return connectors;
    }

    private ConnectorDTO generateConnector(ServiceInstance instance) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(instance.getHost());
        ConnectorDTO connector = new ConnectorDTO();
        connector.setName(instance.getServiceId()+":" + instance.getPort());
        long time = System.currentTimeMillis();
        testReachableAndFillData(inetAddress, connector, time);
        return connector;
    }

    private void testReachableAndFillData(InetAddress inetAddress, ConnectorDTO connector, long time) throws IOException {
        if (inetAddress.isReachable(1000)){
            int delay = (int) (System.currentTimeMillis() - time);
            connector.setDelay(delay);
            connector.setReachable(true);
            connector.setState(getHealthState(delay));
        }else {
            connector.setReachable(false);
        }
    }

    private String getHealthState(int delay){
        if (delay < 100) {
            return "健康";
        }else if (delay<=500){
            return "有问题";
        }else {
            return "延迟过高";
        }
    }
}
