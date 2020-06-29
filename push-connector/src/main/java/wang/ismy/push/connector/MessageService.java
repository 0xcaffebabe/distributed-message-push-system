package wang.ismy.push.connector;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * @author MY
 * @date 2020/6/17 20:01
 */
@Service
@Slf4j
@AllArgsConstructor
public class MessageService {
    private final ClientService clientService;
    private RabbitTemplate rabbitTemplate;

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
        if (tmp.startsWith("heartbeat")){
            tmp = tmp.replaceAll("heartbeat-","");
            clientService.flushClientLiveTime(channel,tmp);
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
}
