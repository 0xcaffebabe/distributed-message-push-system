package wang.ismy.push.client.auth;

import lombok.Data;

/**
 * @author MY
 * @date 2020/10/8 20:46
 */
@Data
public class AuthRequest {
    private String userId;
    private String password;
    private String encryptKey;
}
