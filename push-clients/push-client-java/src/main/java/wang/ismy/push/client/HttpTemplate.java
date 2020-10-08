package wang.ismy.push.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import wang.ismy.push.client.auth.AuthResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 简单的用来进行发送http请求
 * @author MY
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

    public String post(String url, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public <T>T  get(String url, Class<T> returnType) throws IOException, InterruptedException {
        String json = get(url);
        return new ObjectMapper().readValue(json, returnType);
    }

    public <T>T post(String url, Object body, Class<T> returnType) throws IOException, InterruptedException {
        String json = post(url, new ObjectMapper().writeValueAsString(body));
        return new ObjectMapper().readValue(json, returnType);
    }
}
