package wang.ismy.push.client;

import wang.ismy.push.client.auth.AuthManager;

import java.io.IOException;

/**
 * 服务端Connector在客户端的表现
 * @author MY
 * @date 2020/9/9 20:32
 */
public class Connector {

    private String gateway;
    private AuthManager authManager;
    private String host;
    private int port;
    private HttpTemplate httpTemplate = new HttpTemplate();

    public Connector(String gateway,AuthManager authManager, HttpTemplate httpTemplate) {
        this(gateway, authManager);
        this.httpTemplate = httpTemplate;
    }

    public Connector(String gateway, AuthManager authManager) {
        this.gateway = gateway;
        this.authManager = authManager;
    }

    /**
     * 请求分配一个connector
     * @return 返回是否分配成功
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean lookupConnector() throws IOException, InterruptedException {
        ConnectorDTO connectorDTO = httpTemplate.get(gateway + "/connector?token=" + authManager.getToken(), ConnectorDTO.class);
        if (connectorDTO.equals(ConnectorDTO.emptyConnector())) {
            return false;
        }

        host = connectorDTO.getHost();
        port = connectorDTO.getPort();
        return true;
    }

    public boolean isAvailable(){
        return host != null && !"".equals(host) && port > 0;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSecretKey(){
        return authManager.getAuthRequest().getEncryptKey();
    }

    public String getToken(){
        return authManager.getToken();
    }
}
