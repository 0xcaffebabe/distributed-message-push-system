package wang.ismy.push.lookup;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import wang.ismy.push.lookup.entity.AuthRequest;
import wang.ismy.push.lookup.entity.AuthResponse;
import wang.ismy.push.lookup.entity.ConnectorDTO;
import wang.ismy.push.lookup.service.GatewayService;
import wang.ismy.push.lookup.service.RedisService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MY
 * @date 2020/6/17 21:05
 */
@RestController
@Slf4j
@AllArgsConstructor
public class GatewayApi {
    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate;
    private RedisService redisService;
    private GatewayService gatewayService;

    @GetMapping
    public String getConnector(HttpServletRequest request) {
        int retries = 0;
        while (retries <= 5) {
            ServiceInstance service = loadBalancerClient.choose("connector-service");

            if (service == null) {
                log.info("客户 {} 无法获取 Connector", request.getRemoteHost());
                redisService.hashIncr("gateway-stat", "fail");
                return "";
            }

            try {
                String port = restTemplate.getForObject(service.getUri() + "/port", String.class);
                String connector = service.getHost() + ":" + port;
                log.info("客户 {} 经过 {} 次重试获取 Connector {}", request.getRemoteHost(), retries, connector);
                redisService.hashIncr("gateway-stat", "success");
                return connector;
            } catch (Exception e) {
                // 发生异常 既有可能是connector 挂了, 重试
                retries++;
            }
        }
        log.info("客户 {} 经过 {} 次重试无法获取 Connector", request.getRemoteHost(), retries);
        redisService.hashIncr("gateway-stat", "fail");
        return "";
    }

    @GetMapping("connector")
    public ConnectorDTO getConnector(String token){
        return gatewayService.getConnector(token);
    }

    @GetMapping("auth")
    public AuthResponse auth(AuthRequest request){
        AuthResponse failResponse = new AuthResponse();
        failResponse.setSuccess(false);
        if (StringUtils.isEmpty(request.getUserId())){
            failResponse.setMessage("userid 不得为空");
            return failResponse;
        }
        if (StringUtils.isEmpty(request.getPassword())){
            failResponse.setMessage("密码不得为空");
            return failResponse;
        }
        if (StringUtils.isEmpty(request.getEncryptKey())){
            failResponse.setMessage("秘钥不得为空");
            return failResponse;
        }
        return gatewayService.auth(request);
    }
}
