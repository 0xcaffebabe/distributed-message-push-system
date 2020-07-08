package wang.ismy.push.client;

import wang.ismy.push.common.entity.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client("9528");
        client.setMessageHandler(new AutoConfirmMessageHandler(client) {
            @Override
            public void handle0(ClientMessage message) {
                System.out.println("接收到结构化消息:"+message);
            }
        });
        client.connect();
    }
}
