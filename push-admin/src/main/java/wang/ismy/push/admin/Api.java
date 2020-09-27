package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ismy.push.admin.entity.ConnectorDTO;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.admin.service.ConnectorService;
import wang.ismy.push.admin.service.MessageService;

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

    @RequestMapping(value = "message",produces = "application/json;charset=utf8")
    public String sendMessage(String msg,String target) throws JsonProcessingException, InterruptedException {
        var result = messageService.sendTextMessage(target,msg);
        if (!result.ack){
            return "消息"+result.correlationData.getId()+"投递失败:"+result.cause;
        }else {
            return "消息"+result.correlationData.getId()+"投递成功";
        }
    }

    @GetMapping("list")
    public List<MessageDTO> getMessageList(){
        return messageService.getMessageList();
    }

    @GetMapping("connector/list")
    public List<ConnectorDTO> getConnectorList() throws IOException {
        return connectorService.getConnectorList();
    }


}
