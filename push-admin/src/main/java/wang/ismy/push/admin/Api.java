package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ismy.push.admin.entity.MessageDTO;

import java.util.List;

/**
 * @author MY
 * @date 2020/6/23 16:35
 */
@RestController
@AllArgsConstructor
@RequestMapping("api")
public class Api {

    private MessageService messageService;

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

}
