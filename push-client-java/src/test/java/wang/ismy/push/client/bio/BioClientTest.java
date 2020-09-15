package wang.ismy.push.client.bio;

import org.junit.Test;
import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;
import wang.ismy.push.client.Client;
import wang.ismy.push.client.Connector;
import wang.ismy.push.client.factory.ClientFactory;
import wang.ismy.push.client.factory.ManagerFactory;
import wang.ismy.push.client.factory.SocketFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class BioClientTest {

    @Test(expected = IOException.class)
    public void connectFail() throws Exception {
        String userId = "9527";
        Client client = ClientFactory.newBioClient(userId);
        Connector connector = mock(Connector.class);

        when(connector.lookupConnector()).thenThrow(new ConnectException("假装网络异常"));

        client.connect(connector);

    }

    @Test
    public void connectSuccess() throws Exception {
        String userId = "9527";
        Connector connector = mock(Connector.class);
        when(connector.isAvailable()).thenReturn(true);

        SocketFactory socketFactory = mock(SocketFactory.class);
        BioClientThreadAndIoManager manager = mock(BioClientThreadAndIoManager.class);

        ManagerFactory managerFactory = mock(ManagerFactory.class);
        when(managerFactory.newBioManager(isNull(),any())).thenReturn(manager);

        BioClient client = new BioClient(userId,socketFactory, managerFactory);
        client.connect(connector);

        verify(manager).startThread();
    }

    @Test(expected = IllegalStateException.class)
    public void reconnectFail() throws Exception {
        BioClient bioClient = new BioClient(null,null,null);
        bioClient.reconnect();
    }

    @Test
    public void reconnectSuccess() throws Exception {
        String userId = "9527";
        Connector connector = mock(Connector.class);
        when(connector.isAvailable()).thenReturn(true);

        SocketFactory socketFactory = mock(SocketFactory.class);
        BioClientThreadAndIoManager manager = mock(BioClientThreadAndIoManager.class);

        ManagerFactory managerFactory = mock(ManagerFactory.class);
        when(managerFactory.newBioManager(isNull(),any())).thenReturn(manager);

        BioClient client = new BioClient(userId,socketFactory, managerFactory);
        client.connect(connector);
        client.reconnect();

        verify(manager,new Times(2)).startThread();
    }

    @Test(expected = IllegalStateException.class)
    public void sendFail(){
        BioClient client = new BioClient(null,null,null);
        client.send("hello world");
    }

    @Test
    public void sendSuccess() throws Exception {
        String userId = "9527";
        Connector connector = mock(Connector.class);
        when(connector.isAvailable()).thenReturn(true);

        SocketFactory socketFactory = mock(SocketFactory.class);
        BioClientThreadAndIoManager manager = mock(BioClientThreadAndIoManager.class);

        ManagerFactory managerFactory = mock(ManagerFactory.class);
        when(managerFactory.newBioManager(isNull(),any())).thenReturn(manager);

        BioClient client = new BioClient(userId,socketFactory, managerFactory);
        client.connect(connector);

        client.send("hello world");

        verify(manager).send(eq("hello world"));
    }

    @Test
    public void shutdown() throws Exception {
        String userId = "9527";
        Connector connector = mock(Connector.class);
        when(connector.isAvailable()).thenReturn(true);

        SocketFactory socketFactory = mock(SocketFactory.class);
        BioClientThreadAndIoManager manager = mock(BioClientThreadAndIoManager.class);

        ManagerFactory managerFactory = mock(ManagerFactory.class);
        when(managerFactory.newBioManager(isNull(),any())).thenReturn(manager);

        BioClient client = new BioClient(userId,socketFactory, managerFactory);

        client.connect(connector);
        client.close();

        verify(manager).shutdown();
    }
}