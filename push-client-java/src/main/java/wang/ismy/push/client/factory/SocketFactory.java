package wang.ismy.push.client.factory;

import java.io.IOException;
import java.net.Socket;

/**
 * @author MY
 * @date 2020/9/15 17:03
 */
public class SocketFactory {

    public Socket newSocket(String host, int port) throws IOException {
        return new Socket(host,port);
    }

}
