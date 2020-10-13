package wang.ismy.push.client.bio;

import wang.ismy.push.client.Logger;

import java.awt.desktop.OpenURIEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 对连接线程进行管理
 * @author MY
 */
public class BioThreadIoManager {
    private Thread heartbeatThread;
    private Thread ioThread;
    private BioClient client;
    private SocketChannel socketChannel;
    private volatile boolean running = true;
    private volatile boolean authed = false;
    private Logger log = Logger.getInstance();
    public BioThreadIoManager(Socket socket, BioClient client) throws IOException {
        this.client = client;
        this.socketChannel = new SocketChannel(socket);
        createHeartbeatThread();
        createIoThread();
    }

    public SocketChannel getSocketChannel(){
        return this.socketChannel;
    }
    
    public void send(String message){
        if (!authed){
            log.info("未向服务端认证!");
            return;
        }
        socketChannel.writeAndFlush(message);
    }

    public void shutdown(){
        running = false;
        try {
            socketChannel.close();
            log.info("socket 连接已关闭");
        } catch (IOException e){
            log.info("关闭socket发生异常:"+e.getMessage());
        }

    }

    public void startThread(){
        heartbeatThread.start();
        ioThread.start();
    }

    private void createHeartbeatThread(){
        heartbeatThread = new Thread(()->{
            while (running){
                try {
                    this.client.send("heartbeat-"+client.getUserId());
                    Thread.sleep(10000);
                } catch (Exception e) {
                    log.info("心跳线程发生异常:"+e.getMessage());
                }
            }
            log.info("心跳线程已停止");
            shutdown();
        });
    }

    private void createIoThread(){
        ioThread = new Thread(()->{
            while (running){
                try {
                    String s = socketChannel.readLine();
                    log.info(s);
                    if (authed){
                        s = client.decrypt(s);
                    }
                    if ("auth-success".equals(s)){
                        log.info("通过认证 可以向服务端发送消息了");
                        authed = true;
                    }
                    if (client.getMessageHandler() != null) {
                        client.getMessageHandler().handle(s);
                    }
                } catch (IOException e) {
                    // 发生异常　等待一定时间后重试
                    System.err.println("连接发生异常:"+e+" 5s 后重新连接");
                    try {
                        Thread.sleep(5000);
                        client.reconnect();
                        break;
                    } catch (Exception interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
            log.info("io 线程已停止");
            shutdown();
        });
    }
}
