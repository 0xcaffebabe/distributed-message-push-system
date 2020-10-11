package wang.ismy.push.client.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 支持对socket 输入输出流进行读写
 * @author MY
 * @date 2020/9/14 20:58
 */
public class SocketChannel {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public SocketChannel(Socket socket) throws IOException {
        this.socket = socket;
        printWriter = new PrintWriter(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void writeAndFlush(String data){
        printWriter.println(data);
        printWriter.flush();
    }

    public String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}
