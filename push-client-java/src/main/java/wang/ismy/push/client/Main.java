package wang.ismy.push.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author MY
 * @date 2020/6/16 20:19
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connect();
    }
}
