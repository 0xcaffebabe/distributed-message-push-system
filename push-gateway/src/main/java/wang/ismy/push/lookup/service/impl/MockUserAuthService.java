package wang.ismy.push.lookup.service.impl;

import org.springframework.stereotype.Service;
import wang.ismy.push.lookup.service.UserAuthService;

/**
 * @author MY
 * @date 2020/10/6 20:45
 */
@Service
public class MockUserAuthService implements UserAuthService {
    @Override
    public boolean verify(String userId, String password) { return true; }
}
