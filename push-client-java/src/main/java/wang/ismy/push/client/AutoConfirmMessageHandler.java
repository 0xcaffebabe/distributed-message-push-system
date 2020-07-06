package wang.ismy.push.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import wang.ismy.push.common.entity.ClientMessage;

/**
 * @author MY
 * @date 2020/7/6 15:05
 */
public abstract class AutoConfirmMessageHandler implements MessageHandler{

    private final Client client;

    public AutoConfirmMessageHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(String message) {
        try {
            ClientMessage clientMessage = new ObjectMapper().readValue(message, ClientMessage.class);
            client.sendMessage("confirm-"+clientMessage.getMessageId());
            this.handle0(clientMessage);
        } catch(Exception e){
            System.out.println("接收到服务端非结构化消息:"+message);
        }
    }

    /**
     * 留给子类覆写
     * 实现收到消息后的动作
     * @param message
     */
    public abstract void handle0(ClientMessage message);

}
