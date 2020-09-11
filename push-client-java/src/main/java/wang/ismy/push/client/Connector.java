package wang.ismy.push.client;

import java.io.IOException;

/**
 * 服务端Connector在客户端的表现
 * @author MY
 * @date 2020/9/9 20:32
 */
public class Connector {

    private String lookupAddress;
    private String host;
    private int port;
    private HttpTemplate httpTemplate = new HttpTemplate();

    public Connector(String lookupAddress, HttpTemplate httpTemplate) {
        this(lookupAddress);
        this.httpTemplate = httpTemplate;
    }

    public Connector(String lookupAddress) {
        this.lookupAddress = lookupAddress;
    }

    /**
     * 请求分配一个connector
     * @return 返回是否分配成功
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean lookupConnector() throws IOException, InterruptedException {
        String response = httpTemplate.get(lookupAddress);
        String[] splitResult = response.split(":");
        if (splitResult.length != 2) {
            return false;
        }

        host = splitResult[0];
        port = Integer.parseInt(splitResult[1]);
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
}
