package wang.ismy.push.connector.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wang.ismy.push.connector.MessageConfirmDao;
import wang.ismy.push.connector.MessageDao;
import wang.ismy.push.connector.entity.MessageConfirmDO;
import wang.ismy.push.connector.entity.MessageDO;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


/**
 * @author MY
 * @date 2020/6/17 20:01
 */
@Service
@Slf4j
@AllArgsConstructor
public class MessageService {
    private final ClientService clientService;
    private final RabbitTemplate rabbitTemplate;
    private final MessageConfirmDao messageConfirmDao;
    private final MessageDao messageDao;

    @PostConstruct
    public void init() throws IOException {
        var channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(true);
        String exchange = "message";
        channel.exchangeDeclare(exchange, "fanout",true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchange, "");
        channel.basicConsume(queueName,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    wang.ismy.push.common.entity.Message o = (wang.ismy.push.common.entity.Message) new ObjectInputStream(new ByteArrayInputStream(body)).readObject();
                    onMessage(o);
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void read(Channel channel, byte[] bytes){
        String tmp = new String(bytes);
        tmp = tmp.trim();
        if (tmp.startsWith("heartbeat")){
            tmp = tmp.replaceAll("heartbeat-","");
            clientService.flushClientLiveTime(channel,tmp);
        }else if (tmp.startsWith("confirm")){
            tmp = tmp.replaceAll("confirm-","");
            String client = clientService.getClient(channel);
            if (client != null){
                messageConfirmDao.addMessageConfirm(tmp,client);
            }
        }
    }

    public void onMessage(wang.ismy.push.common.entity.Message payload) throws IOException {
        // 发送对象为空，代表是一条广播消息
        log.info("get message:{}",payload);
        if (StringUtils.isEmpty(payload.getTo())){
            clientService.broadcast(new String(payload.getPayload()));
            log.info("广播消息已发起");
        }else {
            clientService.sendMessage(payload.getTo(),new String(payload.getPayload()));
            log.info("已向{}投递消息{}",payload.getTo(),new String(payload.getPayload()));
        }
    }

    /**
     * 定时任务 负责对发送时间在15分钟内的消息 没有接受到的客户端进行消息重试
     */
    @Scheduled(fixedDelay = 5*1000)
    @Async
    public void retrySendMessage(){
        List<MessageDO> list = messageDao.getLast15MinutesMessage();
        log.info("最近 15 分钟 消息数:{}",list.size());
        for (int i = 0; i < list.size(); i++) {
            List<MessageConfirmDO> confirmList = messageConfirmDao.getByMessageId(list.get(i).getMessageId());
            log.info("接收到消息 {} 的用户有 {}",list.get(i).getMessageId(),confirmList.size());
        }
    }
}
