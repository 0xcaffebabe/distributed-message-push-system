package wang.ismy.push.connector;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author MY
 * @date 2020/6/16 20:58
 */
@Service
public class ClientService {

    public void clientActive(Channel channel){}

    public void clientInActive(Channel channel){}

    public void flushClientLiveTime(Channel channel,String userId){}

    public Channel getClient(String userId){
        return null;
    }

    public void broadcast(){}


}
