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
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String connector = getConnector();
        String[] t = connector.split(":");
        String host = t[0];
        int port = Integer.parseInt(t[1]);
        String userId = "9527";
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
                    e.printStackTrace();
                }
            }
        }).start();
        // 这条线程用来接收服务端的消息
        new Thread(()->{
            while (true){

                try {
                    String s = br.readLine();
                    System.out.println("接收到服务消息:"+s);
                } catch (IOException e) {
                    // 发送异常退出程序
                    System.err.println("连接发生异常:"+e);
                    System.exit(-1);
                }

            }
        }).start();
    }

    private static String getConnector() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:30001/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
