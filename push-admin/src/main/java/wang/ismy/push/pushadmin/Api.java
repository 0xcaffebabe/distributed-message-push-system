package wang.ismy.push.pushadmin;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ismy.push.common.entity.Message;

/**
 * @author MY
 * @date 2020/6/23 16:35
 */
@RestController
@AllArgsConstructor
public class Api {

    private RabbitTemplate rabbitTemplate;

    @RequestMapping("message")
    public String sendMessage(String msg,Integer target){
        Message message = new Message();
        message.setPayload(msg.getBytes());
        message.setTo(target);
        rabbitTemplate.convertAndSend("message",null,message);
        return msg;
    }
}
