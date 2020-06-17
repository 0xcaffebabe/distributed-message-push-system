package wang.ismy.push.connector;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author MY
 * @date 2020/6/17 20:01
 */
@Service
@Slf4j
@AllArgsConstructor
public class MessageService {
    private final ClientService clientService;
    public void read(Channel channel, byte[] bytes){
        String tmp = new String(bytes);
        if (tmp.startsWith("heartbeat")){
            tmp = tmp.replaceAll("heartbeat-","");
            clientService.flushClientLiveTime(channel,tmp);
        }
    }
}
