package wang.ismy.push.lookup.entity;

import lombok.Data;

/**
 * @author MY
 * @date 2020/10/6 20:32
 */
@Data
public class AuthResponse {
    private Boolean success;
    private String message;
    private String userId;
    private String token;
    private String connector;
}
