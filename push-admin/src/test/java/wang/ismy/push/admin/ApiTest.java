package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.common.MockUtils;

import java.util.List;

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

        mockMvc.perform(get("/api/message")
                .param("msg",content)
                .param("target",target)

        )
                .andExpect(status().isOk())
                .andExpect(content().string("消息1投递成功"));
    }

    @Test
    void getMessageList() throws Exception {
        var messageService = mock(MessageService.class);
        List<MessageDTO> list = MockUtils.create(MessageDTO.class, 10);
        when(messageService.getMessageList()).thenReturn(list);

        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService)).build();

        mockMvc.perform(get("/api/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(list)));
    }
}