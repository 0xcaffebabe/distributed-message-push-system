package wang.ismy.push.client;

import org.junit.Test;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class HttpTemplateTest {

    @Test
    public void get() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpTemplate httpTemplate = new HttpTemplate(httpClient);

        String url = "http://test.url";
        String response = "response";
        when(
                httpClient.send(
                        argThat(request->request.uri().getHost().equals("test.url")),
                        eq(HttpResponse.BodyHandlers.ofString())
                )
        ).thenReturn(new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 0;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return response;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        });

        var responseResult = httpTemplate.get(url);

        assertEquals(response,responseResult);
    }
}