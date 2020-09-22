package wang.ismy.push.admin;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wang.ismy.push.admin.entity.MessageConfirmDO;
import wang.ismy.push.admin.entity.MessageDTO;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public List<MessageDTO> findLimit10(){
        return jdbcTemplate.query("SELECT t1.message_id, t1.message_target, t1.message_content, t1.create_time, COUNT(*) AS arrival_count " +
                        "FROM tb_message AS t1 JOIN tb_message_confirm AS t2 ON t1.message_id = t2.message_id " +
                        "GROUP BY t1.message_id ORDER BY create_time DESC LIMIT 10",
                (rs, rowNum) -> {
                    MessageDTO dto = new MessageDTO();
                    dto.setMessageId(rs.getString("message_id"));
                    dto.setMessageContent(rs.getString("message_content"));
                    dto.setMessageTarget(rs.getString("message_target"));
                    dto.setArrivalCount(rs.getLong("arrival_count"));
                    dto.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                    return dto;
                }
        );
    }
}
