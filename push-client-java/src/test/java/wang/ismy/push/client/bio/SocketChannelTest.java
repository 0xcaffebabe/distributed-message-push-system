package wang.ismy.push.client.bio;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class SocketChannelTest {

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
    public void writeAndFlush(){
        socketChannel.writeAndFlush("test");
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void readLine() throws IOException {
        assertEquals("test data",socketChannel.readLine());
    }

    @Test
    public void close() throws IOException {
        socketChannel.close();
        verify(socket).close();
    }
}