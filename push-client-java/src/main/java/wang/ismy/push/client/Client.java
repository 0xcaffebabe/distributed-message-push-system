package wang.ismy.push.client;

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
 * @date 2020/6/30 14:54
 */
public class Client {

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private MessageHandler messageHandler;
    private final String userId;

    public Client(String userId) {
        this.userId = userId;
    }

    public void connect() throws Exception{
        String connector;
        try {
             connector = getConnector();
        } catch (Exception e){
            System.out.println("获取connector发生异常:"+e);
            return;
        }
        String[] t = connector.split(":");
        String host = t[0];
        int port = Integer.parseInt(t[1]);
        System.out.println("连接"+connector);
        Socket socket = new Socket(host,port);
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 启动一条线程向connector发送心跳
        new Thread(()->{
            while (true){
                pw.print("heartbeat-"+userId);
                pw.flush();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 这条线程用来接收服务端的消息
        new Thread(()->{
            while (true){

                try {
                    String s = br.readLine();
                    if (messageHandler != null) {
                        messageHandler.handle(s);
                    }
                } catch (IOException e) {
                    // 发生异常重新连接
                    System.err.println("连接发生异常:"+e);
                    break;
                }
            }
            try {
                connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private String getConnector() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:30001/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
