package wang.ismy.push.lookup.service;

/**
 * 连接外部系统 验证用户是否有效
 * @author MY
 * @date 2020/10/6 20:42
 */
public interface UserAuthService {

    /**
     * 验证用户
     * @param userId 用户ID
     * @param password 密码
     * @return 返回用户是否有效
     */
    boolean verify(String userId, String password);
}
