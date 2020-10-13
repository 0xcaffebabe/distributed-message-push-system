package wang.ismy.push.client.bio;

import wang.ismy.push.client.AESUtils;
import wang.ismy.push.client.Client;
import wang.ismy.push.client.Connector;
import wang.ismy.push.client.Logger;
import wang.ismy.push.client.factory.ManagerFactory;
import wang.ismy.push.client.factory.SocketFactory;
import wang.ismy.push.client.message.MessageHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

/**
 * @author MY
 * @date 2020/6/30 14:54
 */
public class BioClient extends Client {

    private Connector connector;
    private final String userId;
    private final SocketFactory socketFactory;
    private final ManagerFactory managerFactory;
    private BioThreadIoManager manager;
    private final Logger log = Logger.getInstance();

    public BioClient(String userId, SocketFactory socketFactory, ManagerFactory managerFactory) {
        this.userId = userId;
        this.socketFactory = socketFactory;
        this.managerFactory = managerFactory;
    }

    @Override
    public void connect(Connector connector) throws Exception {
        if (!connector.isAvailable()) {
            try {
                if (!connector.lookupConnector()) {
                    throw new IOException("无法获取 connector");
                }
            } catch (Exception e) {
                log.info("获取connector发生异常:" + e);
                throw new IOException(e);
            }
        }
        this.connector = connector;
        Socket socket = socketFactory.newSocket(connector.getHost(), connector.getPort());
        manager = managerFactory.newBioManager(socket, this);
        String base64 = Base64.getEncoder().encodeToString(AESUtils.encrypt(connector.getToken().getBytes(), this.connector.getSecretKey()));
        manager.getSocketChannel().writeAndFlush("auth-" + userId + "-" + base64);
        manager.startThread();
    }

    public void reconnect() throws Exception {
        if (connector == null) {
            throw new IllegalStateException("connector is null!!");
        }
        // 重新连接之前必须重新获取connector
        connector.lookupConnector();
        connect(connector);
    }

    @Override
    public void send(String message) {
        if (manager == null) {
            throw new IllegalStateException("manager is null");
        }
        // 加密
        String secretKey = connector.getSecretKey();
        byte[] encrypt = AESUtils.encrypt(message.getBytes(), secretKey);
        String encoded = Base64.getEncoder().encodeToString(encrypt);
        manager.send(encoded);
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

    public String decrypt(String base64EncodeEncrypt){
        byte[] encrypted = Base64.getDecoder().decode(base64EncodeEncrypt);
        return new String(AESUtils.decrypt(encrypted, connector.getSecretKey()));
    }
}

