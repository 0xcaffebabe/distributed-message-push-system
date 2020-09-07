package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiTest {

    private MockMvc mockMvc;

    @Test
    void sendMessage() throws Exception {
        var messageService = mock(MessageService.class);
        String target = "cxk";
        String content = "msg";

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        result.ack =true;
        result.correlationData = new CorrelationData();
        result.correlationData.setId("1");

        when(messageService.sendTextMessage(eq(target),eq(content))).thenReturn(result);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService)).build();

        mockMvc.perform(get("/message")
                .param("msg",content)
                .param("target",target)

        )
                .andExpect(status().isOk())
                .andExpect(content().string("消息1投递成功"));
    }
}