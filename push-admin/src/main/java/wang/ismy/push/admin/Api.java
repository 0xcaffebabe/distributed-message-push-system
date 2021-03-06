package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import wang.ismy.push.admin.entity.ClientDTO;
import wang.ismy.push.admin.entity.ConnectorDTO;
import wang.ismy.push.admin.entity.GatewayStat;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.admin.service.ClientService;
import wang.ismy.push.admin.service.ConnectorService;
import wang.ismy.push.admin.service.MessageService;
import wang.ismy.push.admin.service.RedisService;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MY
 * @date 2020/6/23 16:35
 */
@RestController
@AllArgsConstructor
@RequestMapping("api")
public class Api {

    private final MessageService messageService;

    private final ConnectorService connectorService;

    private final ClientService clientService;

    private final RedisService redisService;

    @RequestMapping(value = "message", produces = "application/json;charset=utf8")
    public String sendMessage(String msg, String target) throws JsonProcessingException, InterruptedException {
        MessageConfirmListener.ConfirmResult result = null;
        if (!StringUtils.isEmpty(target)) {
            result = messageService.sendSingleTextMessage(target, msg);
        } else {
            result = messageService.sendBroadcastTextMessage(msg);
        }

        if (!result.ack) {
            return "消息" + result.correlationData.getId() + "投递失败:" + result.cause;
        } else {
            return "消息" + result.correlationData.getId() + "投递成功";
        }
    }

    @GetMapping("list")
    public List<MessageDTO> getMessageList() {
        return messageService.getMessageList();
    }

    @GetMapping("connector/list")
    public List<ConnectorDTO> getConnectorList() throws IOException {
        return connectorService.getConnectorList();
    }

    @GetMapping("client/list")
    public List<ClientDTO> getClientList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer length) {
        return clientService.getClients(page, length);
    }

    @DeleteMapping(value = "client/{id}")
    public void kickOutClient(@PathVariable String id) throws InterruptedException {
         clientService.kickOut(id);
    }

    @GetMapping("stat/gateway/requests")
    public GatewayStat getGatewayStat(){
        GatewayStat stat = new GatewayStat();
        String success = redisService.hashGet("gateway-stat", "success");
        if (StringUtils.isEmpty(success)){
            success = "0";
        }
        stat.setSuccess(Long.valueOf(success));
        String fail = redisService.hashGet("gateway-stat", "fail");
        if (StringUtils.isEmpty(fail)){
            fail = "0";
        }
        stat.setFail(Long.valueOf(fail));

        return stat;
    }
}
