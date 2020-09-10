package wang.ismy.push.client;

/**
 * 客户端接口定义
 * @author MY
 * @date 2020/9/9 20:30
 */
public interface Client {

    /**
     * 连接到服务端
     * @throws Exception
     */
    void connect(Connector connector) throws Exception;

    void send(String message);

    void close() throws Exception;
}
