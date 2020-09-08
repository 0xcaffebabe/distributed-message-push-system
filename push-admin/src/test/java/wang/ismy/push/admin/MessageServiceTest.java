package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wang.ismy.push.common.entity.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class MessageServiceTest {

    @Test
    void sendTextMessage() throws JsonProcessingException, InterruptedException {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        MessageDAO messageDAO = mock(MessageDAO.class);
        MessageConfirmListener listener = mock(MessageConfirmListener.class);

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        when(listener.await()).thenReturn(result);

        String target = "cxk";
        String text = "hello world";

        MessageService messageService = new MessageService(rabbitTemplate,messageDAO,listener);
        var realResult = messageService.sendTextMessage(target,text);
        assertEquals(realResult,realResult);

        verify(messageDAO).addMessage(argThat(msg->msg.getTo().equals(target)),argThat(msg->msg.getPayload().equals(text)),eq(realResult));

    }
}