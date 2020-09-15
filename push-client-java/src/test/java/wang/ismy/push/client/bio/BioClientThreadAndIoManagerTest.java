package wang.ismy.push.client.bio;

import org.junit.Before;
import org.junit.Test;
import wang.ismy.push.client.Client;
import wang.ismy.push.client.message.MessageHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BioClientThreadAndIoManagerTest {
    Socket socket;
    SocketChannel socketChannel;
    byte[] data = "test data\n".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void before() throws IOException {
        socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        socketChannel = new SocketChannel(socket);
    }

    @Test
    public void testBuild() throws IOException {
        BioClientThreadAndIoManager manager = new BioClientThreadAndIoManager(socket, null);
    }

    @Test
    public void send() throws IOException {
        BioClientThreadAndIoManager manager = new BioClientThreadAndIoManager(socket, null);
        String msg = "hello world";

        manager.send(msg);

        assertEquals(msg, new String(outputStream.toByteArray()));
        manager.shutdown();
    }

    @Test
    public void verifyHeartbeat() throws IOException, InterruptedException {
        String heartbeatMsg = "heartbeat-9527";
        BioClient client = mock(BioClient.class);
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(client.getUserId()).thenReturn("9527");
        when(client.getMessageHandler()).thenReturn(messageHandler);

        BioClientThreadAndIoManager manager = new BioClientThreadAndIoManager(socket, client);
        manager.startThread();

        Thread.sleep(12000);

        assertEquals(heartbeatMsg + heartbeatMsg, new String(outputStream.toByteArray()));
        manager.shutdown();
    }

    @Test
    public void verifyIoThread() throws IOException, InterruptedException {
        String msg = "test data";
        BioClient client = mock(BioClient.class);
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(client.getMessageHandler()).thenReturn(messageHandler);

        BioClientThreadAndIoManager manager = new BioClientThreadAndIoManager(socket, client);
        manager.startThread();
        Thread.sleep(1000);

        verify(messageHandler).handle(msg);
        manager.shutdown();
    }
}