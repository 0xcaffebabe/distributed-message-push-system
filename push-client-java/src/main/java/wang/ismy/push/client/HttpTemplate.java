package wang.ismy.push.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 简单的用来进行发送http请求
 */
public class HttpTemplate {
    private HttpClient httpClient = HttpClient.newBuilder().build();

    public HttpTemplate() {}

    public HttpTemplate(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
