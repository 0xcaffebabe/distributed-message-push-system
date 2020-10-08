package wang.ismy.push.lookup;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
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

    private final GatewayService gatewayService;

    @GetMapping("connector")
    public ConnectorDTO getConnector(@RequestParam String token){
        return gatewayService.getConnector(token);
    }

    @PostMapping("auth")
    public AuthResponse auth(@RequestBody AuthRequest request){
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
