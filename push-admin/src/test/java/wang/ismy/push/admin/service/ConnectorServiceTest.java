package wang.ismy.push.admin.service;

import org.junit.Test;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;
import wang.ismy.push.admin.entity.ConnectorDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class ConnectorServiceTest {

    @Test
    public void getConnectorListSuccess() throws IOException {
        DiscoveryClient discoveryClient = mock(DiscoveryClient.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject(anyString(),eq(Long.class))).thenReturn(100L);
        ConnectorService connectorService = new ConnectorService(discoveryClient, restTemplate);

        List<ServiceInstance> instances = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DefaultServiceInstance instance =
                    new DefaultServiceInstance(null, "connector-service","127.0.0.1",i,true);
            instances.add(instance);
        }

        when(discoveryClient.getInstances(eq("connector-service")))
                .thenReturn(instances);

        List<ConnectorDTO> list = connectorService.getConnectorList();
        assertEquals(10, list.size());
        assertEquals("健康",list.get(0).getState());
        assertTrue(list.get(0).getReachable());
        assertEquals(100L, (long) list.get(0).getUsers());
    }

    @Test
    public void getConnectorListSomeCantReach() throws IOException {
        DiscoveryClient discoveryClient = mock(DiscoveryClient.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject(anyString(),eq(Long.class))).thenReturn(100L);
        ConnectorService connectorService = new ConnectorService(discoveryClient, restTemplate);

        List<ServiceInstance> instances = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            DefaultServiceInstance instance =
                    new DefaultServiceInstance(null, "connector-service","127.0.0.1",i,true);
            instances.add(instance);
        }
        instances.add(new DefaultServiceInstance(null, "connector-service","172.17.0.188",1999,true));

        when(discoveryClient.getInstances(eq("connector-service")))
                .thenReturn(instances);

        List<ConnectorDTO> list = connectorService.getConnectorList();
        assertEquals(10, list.size());
        assertEquals("健康",list.get(0).getState());
        assertEquals(100L, (long) list.get(0).getUsers());
        assertTrue(list.get(0).getReachable());

        assertFalse(list.stream().filter(c -> !c.getReachable()).collect(Collectors.toList()).get(0).getReachable());
    }
}