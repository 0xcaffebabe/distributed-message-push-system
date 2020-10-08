package wang.ismy.push.lookup.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wang.ismy.push.lookup.entity.AuthRequest;
import wang.ismy.push.lookup.entity.AuthResponse;
import wang.ismy.push.lookup.entity.ConnectorDTO;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GatewayService {

    private final UserAuthService authService;
    private final RedisService redisService;

    public AuthResponse auth(AuthRequest request){
        AuthResponse response = new AuthResponse();
        if (!authService.verify(request.getUserId(), request.getPassword())){
            response.setMessage("认证失败");
            response.setSuccess(false);
            return response;
        }

        response.setMessage("认证成功");
        response.setSuccess(true);

        String token = UUID.randomUUID().toString();
        redisService.setToken(request.getUserId(), token);
        response.setToken(token);

        redisService.setEncryptKey(request.getUserId(), request.getEncryptKey());
        return response;
    }

    public ConnectorDTO getConnector(String token){
        // todo
        return null;
    }
}
