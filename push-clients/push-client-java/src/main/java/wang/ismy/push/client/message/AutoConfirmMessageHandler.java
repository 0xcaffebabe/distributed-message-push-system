package wang.ismy.push.client.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import wang.ismy.push.client.Client;


import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author MY
 * @date 2020/7/6 15:05
 */
public abstract class AutoConfirmMessageHandler implements MessageHandler {

    private final Client client;
    private final Set<String> messageSet = new ConcurrentSkipListSet<>();
    public AutoConfirmMessageHandler(Client client) {
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
            client.send("confirm-"+clientMessage.getMessageId());
            this.handle0(clientMessage);
            messageSet.add(clientMessage.getMessageId());
        } catch(Exception e){
            handleUnStructMsg(message);
        }
    }

    /**
     * 留给子类覆写
     * 实现收到消息后的动作
     * @param message
     */
    public abstract void handle0(ClientMessage message);

    private void handleUnStructMsg(String msg){

        if (msg.startsWith("kickout")){
            System.out.println("被踢出客户端了！！！");
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("接收到非结构化消息:" + msg);
        }
    }
}
