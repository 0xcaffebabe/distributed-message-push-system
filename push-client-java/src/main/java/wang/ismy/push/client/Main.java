package wang.ismy.push.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",40001);
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("hello world");
        pw.flush();
        socket.close();
    }
}
