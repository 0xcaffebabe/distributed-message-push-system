package wang.ismy.push.client;

import wang.ismy.push.client.message.MessageHandler;

/**
 * 客户端接口定义
 * @author MY
 * @date 2020/9/9 20:30
 */
public abstract class Client {

    protected MessageHandler messageHandler;

    public abstract void connect(Connector connector) throws Exception;

    public abstract void send(String message);

    protected void onMessage(String msg){
        if (messageHandler != null) {
            messageHandler.handle(msg);
        }
    }

    public abstract void close() throws Exception;

    public void setMessageHandler(MessageHandler handler){
        this.messageHandler = handler;
    }
}
