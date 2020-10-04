package wang.ismy.push.client.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import wang.ismy.push.client.Client;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class AutoConfirmMessageHandlerTest {

    @Test
    public void handleUnstructuredMessage(){
        String message = "test message";
        Client client = mock(Client.class);

        AutoConfirmMessageHandler handler = new AutoConfirmMessageHandler(client) {
            @Override
            public void handle0(ClientMessage message) {

            }
        };
        handler.handle(message);
    }

    @Test
    public void handleStructuredMessage() throws JsonProcessingException {
        String message = new ObjectMapper().writeValueAsString(Map.of("messageId","1","messageType","type","payload","test"));
        Client client = mock(Client.class);

        AutoConfirmMessageHandler handler = new AutoConfirmMessageHandler(client) {
            @Override
            public void handle0(ClientMessage message) {
                assertEquals("1",message.getMessageId());
                assertEquals("type",message.getMessageType());
                assertEquals("test",message.getPayload());
            }
        };
        handler.handle(message);
        verify(client).send(eq("confirm-1"));
    }
}