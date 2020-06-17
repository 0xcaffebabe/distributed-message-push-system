package wang.ismy.push.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String userId = "9527";
        Socket socket = new Socket("localhost",40001);
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        new Thread(()->{
            while (true){
                String s = null;
                try {
                    s = br.readLine();
                    System.out.println("接收到服务消息:"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
