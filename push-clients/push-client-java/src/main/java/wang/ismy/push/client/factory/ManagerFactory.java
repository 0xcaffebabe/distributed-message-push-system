package wang.ismy.push.client.factory;

import wang.ismy.push.client.bio.BioClient;
import wang.ismy.push.client.bio.BioThreadIoManager;

import java.io.IOException;
import java.net.Socket;

/**
 * @author MY
 * @date 2020/9/15 17:05
 */
public class ManagerFactory {
    public BioThreadIoManager newBioManager(Socket socket, BioClient client) throws IOException {
        return new BioThreadIoManager(socket,client);
    }
}
