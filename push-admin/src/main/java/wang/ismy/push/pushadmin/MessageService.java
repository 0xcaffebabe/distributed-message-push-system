package wang.ismy.push.pushadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

import java.util.UUID;

/**
 * @author MY
 * @date 2020/7/1 15:22
 */
@Service
@AllArgsConstructor
public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    public void sendTextMessage(String target, String text) throws JsonProcessingException {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setPayload(text);
        clientMessage.setMessageType("TEXT");
        clientMessage.setMessageId(UUID.randomUUID().toString());
        Message message = new Message();
        message.setPayload(new ObjectMapper().writeValueAsBytes(clientMessage));
        message.setTo(target);

        rabbitTemplate.convertAndSend("message",null,message);
    }
}
