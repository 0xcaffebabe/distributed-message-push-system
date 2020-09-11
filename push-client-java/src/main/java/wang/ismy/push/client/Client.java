package wang.ismy.push.client;

import wang.ismy.push.client.message.MessageHandler;

/**
 * 客户端接口定义
 * @author MY
 * @date 2020/9/9 20:30
 */
public interface Client {

    void connect(Connector connector) throws Exception;

    void send(String message);

    void close() throws Exception;

    void setMessageHandler(MessageHandler handler);
}
