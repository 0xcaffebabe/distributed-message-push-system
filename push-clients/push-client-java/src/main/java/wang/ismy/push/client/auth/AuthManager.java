package wang.ismy.push.client.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import wang.ismy.push.client.HttpTemplate;

import java.io.IOException;

/**
 * @author MY
 * @date 2020/10/8 20:43
 */
@Data
public class AuthManager {
    private String gateway;
    private String token;
    private final AuthRequest authRequest;
    private final HttpTemplate httpTemplate;

    public AuthManager(String gateway, AuthRequest authRequest, HttpTemplate httpTemplate) {
        this.gateway = gateway;
        this.authRequest = authRequest;
        this.httpTemplate = httpTemplate;
    }

    public void auth() throws Exception {
        AuthResponse authResponse = httpTemplate.post(gateway + "/auth", authRequest, AuthResponse.class);
        if (authResponse.getSuccess()) {
            this.token = authResponse.getToken();
        }else{
            System.out.println("认证失败：" + authResponse);
        }
    }

}
