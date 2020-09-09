package wang.ismy.push.client;

import java.net.InetAddress;

/**
 * 服务端Connector在客户端的表现
 * @author MY
 * @date 2020/9/9 20:32
 */
public class Connector {

    private String lookupAddress;
    private InetAddress connectorAddress;
    private int connectorPort;

    public Connector(String lookupAddress) {
        this.lookupAddress = lookupAddress;
    }

}
