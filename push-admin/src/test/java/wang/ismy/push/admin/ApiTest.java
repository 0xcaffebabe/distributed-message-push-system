package wang.ismy.push.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wang.ismy.push.admin.entity.ClientDTO;
import wang.ismy.push.admin.entity.ConnectorDTO;
import wang.ismy.push.admin.entity.GatewayStat;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.admin.service.ClientService;
import wang.ismy.push.admin.service.ConnectorService;
import wang.ismy.push.admin.service.MessageService;
import wang.ismy.push.admin.service.RedisService;
import wang.ismy.push.common.MockUtils;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiTest {

    private MockMvc mockMvc;

    @Test
    void sendSingleTextMessage() throws Exception {
        var messageService = mock(MessageService.class);
        String target = "cxk";
        String content = "msg";

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        result.ack =true;
        result.correlationData = new CorrelationData();
        result.correlationData.setId("1");

        when(messageService.sendSingleTextMessage(eq(target),eq(content))).thenReturn(result);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService, null,null, null)).build();

        mockMvc.perform(get("/api/message")
                .param("msg",content)
                .param("target",target)

        )
                .andExpect(status().isOk())
                .andExpect(content().string("消息1投递成功"));
    }

    @Test
    void sendBroadcastTextMessage() throws Exception {
        var messageService = mock(MessageService.class);
        String target = "";
        String content = "msg";

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        result.ack =true;
        result.correlationData = new CorrelationData();
        result.correlationData.setId("1");

        when(messageService.sendBroadcastTextMessage(eq(content))).thenReturn(result);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService, null,null, null)).build();

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

        mockMvc = MockMvcBuilders.standaloneSetup(new Api(messageService, null,null, null)).build();

        mockMvc.perform(get("/api/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(list)));
    }

    @Test
    void getConnectorList() throws Exception {
        ConnectorService connectorService = mock(ConnectorService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, connectorService,null, null)).build();

        var list = MockUtils.create(ConnectorDTO.class,10);
        when(connectorService.getConnectorList()).thenReturn(list);

        mockMvc.perform(get("/api/connector/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.parse(list)));
    }

    @Test
    void getClientListWithDefaultParams() throws Exception {
        ClientService clientService = mock(ClientService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,clientService, null)).build();

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
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,clientService, null)).build();

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

    @Test
    public void kickOutClient() throws Exception {
        ClientService clientService = mock(ClientService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,clientService, null)).build();

        mockMvc.perform(delete("/api/client/9527"))
                .andExpect(status().isOk());
        verify(clientService).kickOut(eq("9527"));
    }

    @Test
    public void getGatewayStatNormal() throws Exception {
        RedisService redisService = mock(RedisService.class);
        when(redisService.hashGet(anyString(),anyString())).thenReturn("1");
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,null, redisService)).build();

        GatewayStat stat = new GatewayStat();
        stat.setFail(1L);
        stat.setSuccess(1L);

        mockMvc.perform(get("/api/stat/gateway/requests"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(stat)));
    }

    @Test
    public void getGatewayStatWhenNull() throws Exception {
        RedisService redisService = mock(RedisService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new Api(null, null,null, redisService)).build();

        GatewayStat stat = new GatewayStat();
        stat.setFail(0L);
        stat.setSuccess(0L);

        mockMvc.perform(get("/api/stat/gateway/requests"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(stat)));
    }
}