package wang.ismy.push.lookup.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wang.ismy.push.lookup.entity.AuthRequest;
import wang.ismy.push.lookup.entity.AuthResponse;
import wang.ismy.push.lookup.entity.ConnectorDTO;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class GatewayService {

    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;
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
        response.setUserId(request.getUserId());

        redisService.setEncryptKey(request.getUserId(), request.getEncryptKey());
        return response;
    }

    public ConnectorDTO getConnector(String token){
        String userId = redisService.getUserIdBy(token);
        boolean verified = !StringUtils.isEmpty(userId);
        if (!verified){
            return ConnectorDTO.emptyConnector();
        }

        return chooseConnector(userId);
    }

    private ConnectorDTO chooseConnector(String userId){
        int retries = 0;
        while (retries <= 5) {
            ServiceInstance service = loadBalancerClient.choose("connector-service");

            if (service == null) {
                log.info("客户 {} 无法获取 Connector", userId);
                redisService.hashIncr("gateway-stat", "fail");
                return ConnectorDTO.emptyConnector();
            }

            try {
                String port = restTemplate.getForObject(service.getUri() + "/port", String.class);
                ConnectorDTO connector = new ConnectorDTO();
                connector.setPort(Integer.parseInt(port));
                connector.setHost(service.getHost());
                log.info("客户 {} 经过 {} 次重试获取 Connector {}", userId, retries, connector);
                redisService.hashIncr("gateway-stat", "success");
                return connector;
            } catch (Exception e) {
                // 发生异常 既有可能是connector 挂了, 重试
                retries++;
            }
        }
        log.info("客户 {} 经过 {} 次重试无法获取 Connector", userId, retries);
        redisService.hashIncr("gateway-stat", "fail");
        return ConnectorDTO.emptyConnector();
    }
}
