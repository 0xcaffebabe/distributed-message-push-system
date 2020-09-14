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
public class BioClient extends Client {

    private Connector connector;
    private final String userId;
    private BioClientThreadAndIoManager manager;
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
        this.connector = connector;
        Socket socket = new Socket(connector.getHost(),connector.getPort());
        manager = new BioClientThreadAndIoManager(socket,this);
        manager.startThread();
    }

    public void reconnect() throws Exception {
        if (connector == null){
            throw new IllegalStateException("connector is null!!");
        }
        // 重新连接之前必须重新获取connector
        connector.lookupConnector();
        connect(connector);
    }

    @Override
    public void send(String message){
        manager.send(message);
    }

    @Override
    public void close() throws Exception {
        if (manager != null) {
            manager.shutdown();
        }
    }

    public String getUserId() {
        return userId;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}

/**
 * 对连接线程进行管理
 */
class BioClientThreadAndIoManager {
    private Thread heartbeatThread;
    private Thread ioThread;
    private BioClient client;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private volatile boolean running = true;
    private Logger log = Logger.getInstance();
    public BioClientThreadAndIoManager(Socket socket, BioClient client) throws IOException {
        this.client = client;
        this.socket = socket;
        printWriter = new PrintWriter(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        createHeartbeatThread();
        createIoThread();
    }

    public void send(String message){
        printWriter.println(message);
        printWriter.flush();
    }

    public void shutdown(){
        running = false;
        try {
            socket.close();
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
                send("heartbeat-"+client.getUserId());
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
                    String s = bufferedReader.readLine();
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
