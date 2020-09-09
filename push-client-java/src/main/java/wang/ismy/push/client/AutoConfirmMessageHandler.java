package wang.ismy.push.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import wang.ismy.push.common.entity.ClientMessage;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author MY
 * @date 2020/7/6 15:05
 */
public abstract class AutoConfirmMessageHandler implements MessageHandler{

    private final BioClient client;
    private final Set<String> messageSet = new ConcurrentSkipListSet<>();
    public AutoConfirmMessageHandler(BioClient client) {
        this.client = client;
    }

    @Override
    public void handle(String message) {
        try {
            ClientMessage clientMessage = new ObjectMapper().readValue(message, ClientMessage.class);
            if (messageSet.contains(clientMessage.getMessageId())){
                System.out.println("消息重复："+clientMessage.toString());
                return;
            }
            client.sendMessage("confirm-"+clientMessage.getMessageId());
            this.handle0(clientMessage);
            messageSet.add(clientMessage.getMessageId());
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
