package wang.ismy.push.admin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wang.ismy.push.admin.MessageConfirmListener;
import wang.ismy.push.admin.entity.ClientDTO;
import wang.ismy.push.common.entity.ServerMessage;
import wang.ismy.push.common.enums.ServerMessageTypeEnum;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientServiceTest {

    @Autowired
    ClientService clientService;

    private ServerMessage tmp;

    @Test
    public void getClientsWhenPageLessThan1() {
        RedisService redisService = mock(RedisService.class);
        ClientService clientService = new ClientService(redisService, null, null);
        int page = -1;
        int length = 100;
        Set<String> mockSet = new HashSet<>();
        for (int i = 0; i < length; i++) {
            mockSet.add("connector-40002-" + i);
        }
        when(redisService.getAllClient(eq(0), eq(length)))
                .thenReturn(mockSet);
        when(redisService.getTTL(any())).thenReturn(100);

        List<ClientDTO> clients = clientService.getClients(page, length);
        clients.sort(Comparator.comparing(o -> Integer.valueOf(o.getId())));

        assertEquals(length, clients.size());

        for (int i = 0; i < length; i++) {
            assertEquals("connector40002", clients.get(i).getConnector());
            assertEquals(i + "", clients.get(i).getId());
        }

    }

    @Test
    public void getClientsWhenPageIs2() {
        RedisService redisService = mock(RedisService.class);
        ClientService clientService = new ClientService(redisService, null, null);
        int page = 2;
        int length = 100;
        Set<String> mockSet = new HashSet<>();
        for (int i = 0; i < length; i++) {
            mockSet.add("connector-40002-" + i);
        }
        when(redisService.getAllClient(eq(100), eq(length)))
                .thenReturn(mockSet);
        when(redisService.getTTL(any())).thenReturn(100);

        List<ClientDTO> clients = clientService.getClients(page, length);
        clients.sort(Comparator.comparing(o -> Integer.valueOf(o.getId())));

        assertEquals(length, clients.size());

        for (int i = 0; i < length; i++) {
            assertEquals("connector40002", clients.get(i).getConnector());
            assertEquals(i + "", clients.get(i).getId());
        }

    }

    @Test
    public void kickOutClient() throws InterruptedException {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        MessageConfirmListener listener = mock(MessageConfirmListener.class);
        ClientService clientService = new ClientService(null, rabbitTemplate, listener);
        clientService.kickOut("9527");
        verify(rabbitTemplate).convertAndSend(eq("message"), eq(null),
                argThat((ServerMessage m) -> {
                    tmp = m;
                    return m.getTo().equals("9527") && m.getMessageType().equals(ServerMessageTypeEnum.KICK_OUT_MESSAGE_TYPE);
                }));
    }
}