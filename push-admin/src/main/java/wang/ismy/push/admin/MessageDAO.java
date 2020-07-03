package wang.ismy.push.admin;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

/**
 * @author MY
 * @date 2020/7/3 16:18
 */
@AllArgsConstructor
@Repository
public class MessageDAO {

    private final JdbcTemplate jdbcTemplate;

    public int addMessage(Message message, ClientMessage clientMessage, MessageConfirmListener.ConfirmResult result){
        return jdbcTemplate.update("INSERT INTO tb_message(message_id,message_content,create_time,is_send,message_target) VALUES(?,?,NOW(),?,?)",
                clientMessage.getMessageId(),
                new String(message.getPayload()),
                result.ack,
                message.getTo()
                );
    }
}
