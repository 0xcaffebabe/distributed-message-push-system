package wang.ismy.push.client;

import wang.ismy.push.client.auth.AuthManager;
import wang.ismy.push.client.auth.AuthRequest;
import wang.ismy.push.client.factory.ClientFactory;
import wang.ismy.push.client.factory.ConnectorFactory;
import wang.ismy.push.client.message.AutoConfirmMessageHandler;
import wang.ismy.push.client.message.ClientMessage;

import java.util.Random;
import java.util.UUID;


/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Client client = ClientFactory.newBioClient("9527");
        String gateway = "http://192.168.1.12:30001";

        AuthRequest request = new AuthRequest();
        request.setUserId("9527");
        request.setPassword("123");
        request.setEncryptKey(AESUtils.generateKey("123").getFormat());

        AuthManager authManager = new AuthManager(gateway,request,new HttpTemplate());
        authManager.auth();

        Connector connector = ConnectorFactory.newConnector(gateway,authManager);
        client.setMessageHandler(new AutoConfirmMessageHandler(client) {
            @Override
            public void handle0(ClientMessage message) {
                System.out.println("接收到结构化消息:"+message);
            }
        });
        client.connect(connector);
    }
}
