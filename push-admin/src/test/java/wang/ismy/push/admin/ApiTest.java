package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wang.ismy.push.admin.entity.ClientDTO;
import wang.ismy.push.admin.entity.ConnectorDTO;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.admin.service.ClientService;
import wang.ismy.push.admin.service.ConnectorService;
import wang.ismy.push.admin.service.MessageService;
import wang.ismy.push.common.MockUtils;

import java.util.List;

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
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService, null,null)).build();

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

        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService, null,null)).build();

        mockMvc.perform(get("/api/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(list)));
    }

    @Test
    void getConnectorList() throws Exception {
        ConnectorService connectorService = mock(ConnectorService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, connectorService,null)).build();

        var list = MockUtils.create(ConnectorDTO.class,10);
        when(connectorService.getConnectorList()).thenReturn(list);

        mockMvc.perform(get("/api/connector/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(list)));
    }

    @Test
    void getClientListWithDefaultParams() throws Exception {
        ClientService clientService = mock(ClientService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,clientService)).build();

        var list = MockUtils.create(ConnectorDTO.class,10);
        List<ClientDTO> mockList = MockUtils.create(ClientDTO.class, 100);
        when(clientService.getClients(eq(1),eq(100))).thenReturn(mockList);

        mockMvc.perform(get("/api/client/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(mockList)));
    }

    @Test
    void getClientListWithParams() throws Exception {
        ClientService clientService = mock(ClientService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,clientService)).build();

        var list = MockUtils.create(ConnectorDTO.class,10);
        List<ClientDTO> mockList = MockUtils.create(ClientDTO.class, 100);
        int page = 2;
        int length = 50;
        when(clientService.getClients(eq(page),eq(length))).thenReturn(mockList);

        mockMvc.perform(get("/api/client/list")
                        .param("page", page + "")
                        .param("length", length + "")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(mockList)));
    }
}