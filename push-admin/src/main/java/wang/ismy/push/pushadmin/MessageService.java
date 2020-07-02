package wang.ismy.push.pushadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author MY
 * @date 2020/7/1 15:22
 */
@Service
@Slf4j
public class MessageService {

    private RabbitTemplate rabbitTemplate;

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private MessageConfirmListener confirmListener = new MessageConfirmListener();

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(confirmListener);
    }

    public synchronized MessageConfirmListener.ConfirmResult sendTextMessage(String target, String text) throws JsonProcessingException, InterruptedException {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setPayload(text);
        clientMessage.setMessageType("TEXT");
        clientMessage.setMessageId(UUID.randomUUID().toString());
        Message message = new Message();
        message.setPayload(new ObjectMapper().writeValueAsBytes(clientMessage));
        message.setTo(target);

        rabbitTemplate.convertAndSend("message",null,message,new CorrelationData(clientMessage.getMessageId()));
        return confirmListener.await();
    }
}
