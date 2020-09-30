package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import wang.ismy.push.admin.service.MessageService;
import wang.ismy.push.common.enums.ServerMessageTypeEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class MessageServiceTest {

    @Test
    void sendSingleTextMessage() throws JsonProcessingException, InterruptedException {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        MessageDAO messageDAO = mock(MessageDAO.class);
        MessageConfirmListener listener = mock(MessageConfirmListener.class);

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        when(listener.await()).thenReturn(result);

        String target = "cxk";
        String text = "hello world";

        MessageService messageService = new MessageService(rabbitTemplate,messageDAO,listener);
        var realResult = messageService.sendSingleTextMessage(target,text);
        assertEquals(realResult,realResult);

        verify(messageDAO).addMessage(argThat(msg->msg.getTo().equals(target) && msg.getMessageType().equals(ServerMessageTypeEnum.SINGLE_MESSAGE_TYPE)),argThat(msg->msg.getPayload().equals(text)),eq(realResult));

    }

    @Test
    void sendBroadcastTextMessage() throws JsonProcessingException, InterruptedException {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        MessageDAO messageDAO = mock(MessageDAO.class);
        MessageConfirmListener listener = mock(MessageConfirmListener.class);

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        when(listener.await()).thenReturn(result);

        String text = "hello world";

        MessageService messageService = new MessageService(rabbitTemplate,messageDAO,listener);
        var realResult = messageService.sendBroadcastTextMessage(text);
        assertEquals(realResult,realResult);

        verify(messageDAO).addMessage(argThat(msg->msg.getTo().equals("")&& msg.getMessageType().equals(ServerMessageTypeEnum.BROADCAST_MESSAGE_TYPE)),argThat(msg->msg.getPayload().equals(text)),eq(realResult));

    }
}