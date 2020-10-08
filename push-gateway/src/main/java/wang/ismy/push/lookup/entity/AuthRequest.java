package wang.ismy.push.lookup.entity;

import lombok.Data;

/**
 * @author MY
 * @date 2020/10/6 20:33
 */
@Data
public class AuthRequest {
    private String userId;
    private String password;
    private String encryptKey;
}
