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
import wang.ismy.push.common.entity.ServerMessage;
import wang.ismy.push.common.enums.ServerMessageTypeEnum;

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

    public synchronized MessageConfirmListener.ConfirmResult sendSingleTextMessage(String target, String text)
            throws JsonProcessingException, InterruptedException {
        ClientMessage clientMessage = generateTextClientMessage(text);
        ServerMessage message = generateServerMessage(target, clientMessage, ServerMessageTypeEnum.SINGLE_MESSAGE_TYPE);

        MessageConfirmListener.ConfirmResult result = waitMessageDeliveryResult(clientMessage, message);

        recordMessageToDb(clientMessage, message, result);
        return result;
    }

    public synchronized MessageConfirmListener.ConfirmResult sendBroadcastTextMessage(String text)
            throws JsonProcessingException, InterruptedException {
        ClientMessage clientMessage = generateTextClientMessage(text);
        ServerMessage message = generateServerMessage("", clientMessage, ServerMessageTypeEnum.BROADCAST_MESSAGE_TYPE);

        MessageConfirmListener.ConfirmResult result = waitMessageDeliveryResult(clientMessage, message);

        recordMessageToDb(clientMessage, message, result);
        return result;
    }

    private void recordMessageToDb(ClientMessage clientMessage, ServerMessage message, MessageConfirmListener.ConfirmResult result) {
        messageDAO.addMessage(message, clientMessage, result);
    }

    public List<MessageDTO> getMessageList(){
        return messageDAO.findLimit10();
    }

    private MessageConfirmListener.ConfirmResult waitMessageDeliveryResult(ClientMessage clientMessage, ServerMessage message)
            throws InterruptedException {
        rabbitTemplate.convertAndSend("message", null, message, new CorrelationData(clientMessage.getMessageId()));
        return confirmListener.await();
    }

    private ServerMessage generateServerMessage(String target, ClientMessage clientMessage, ServerMessageTypeEnum singleMessageType)
            throws JsonProcessingException {
        ServerMessage message = new ServerMessage();
        message.setPayload(new ObjectMapper().writeValueAsBytes(clientMessage));
        message.setMessageType(singleMessageType);
        message.setTo(target);
        return message;
    }

    private ClientMessage generateTextClientMessage(String text) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setPayload(text);
        clientMessage.setMessageType("TEXT");
        clientMessage.setMessageId(UUID.randomUUID().toString());
        return clientMessage;
    }
}
