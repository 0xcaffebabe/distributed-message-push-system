package wang.ismy.push.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import wang.ismy.push.admin.MessageConfirmListener;
import wang.ismy.push.admin.MessageDAO;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * @author MY
 * @date 2020/7/1 15:22
 */
@Service
@Slf4j
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageDAO messageDAO;
    private final MessageConfirmListener confirmListener;

    public MessageService(RabbitTemplate rabbitTemplate,MessageDAO messageDAO,MessageConfirmListener listener) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageDAO = messageDAO;
        confirmListener = listener;
    }

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
        var result = confirmListener.await();
        // 向数据库写入该条消息
        messageDAO.addMessage(message,clientMessage,result);
        return result;
    }

    public List<MessageDTO> getMessageList(){
        return messageDAO.findLimit10();
    }
}
