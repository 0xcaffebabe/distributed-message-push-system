package wang.ismy.push.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import wang.ismy.push.connector.entity.ResourceInfo;
import wang.ismy.push.connector.service.ClientService;
import wang.ismy.push.connector.service.ConnectorService;
import wang.ismy.push.connector.service.MessageService;

/**
 * @author MY
 * @date 2020/6/17 20:41
 */
@RestController
public class Api {

    @Value("${connector.port}")
    private int port;

    private final ClientService clientService;

    private final ConnectorService connectorService;

    public Api(ClientService clientService, ConnectorService connectorService) {
        this.clientService = clientService;
        this.connectorService = connectorService;
    }

    @GetMapping("port")
    public int getConnectorPort(){
        return port;
    }

    @GetMapping("users")
    public long getOnlineUserCount(){ return clientService.getClients().size(); }

    @GetMapping("resources")
    public ResourceInfo getResourceInfo(){ return connectorService.getResourceInfo(); }
}
