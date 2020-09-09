package wang.ismy.push.client;

import wang.ismy.push.common.entity.ClientMessage;


/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BioClient client = new BioClient("9528");
        client.setMessageHandler(new AutoConfirmMessageHandler(client) {
            @Override
            public void handle0(ClientMessage message) {
                System.out.println("接收到结构化消息:"+message);
            }
        });
        client.connect();
    }
}
