package wang.ismy.push.client;

import wang.ismy.push.client.message.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author MY
 * @date 2020/6/30 14:54
 */
public class BioClient implements Client {

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private MessageHandler messageHandler;
    private final String userId;
    private PrintWriter printWriter;
    private final Logger log = Logger.getInstance();

    public BioClient(String userId) {
        this.userId = userId;
    }

    @Override
    public void connect(Connector connector) throws Exception{
        if (!connector.isAvailable()) {
            try {
                connector.lookupConnector();
            } catch (Exception e){
                log.info("获取connector发生异常:"+e);
                return;
            }
        }

        Socket socket = new Socket(connector.getHost(),connector.getPort());
        printWriter = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 启动一条线程向connector发送心跳
        new Thread(()->{
            while (true){
                printWriter.print("heartbeat-"+userId);
                printWriter.flush();
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
                connect(connector);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void send(String message){
        printWriter.println(message);
        printWriter.flush();
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException();
    }
}
