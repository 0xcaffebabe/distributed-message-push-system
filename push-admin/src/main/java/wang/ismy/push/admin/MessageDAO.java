package wang.ismy.push.admin;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
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
        return jdbcTemplate.query("SELECT * FROM tb_message ORDER BY create_time DESC LIMIT 10",
                new RowMapper<MessageDTO>() {
                    @Override
                    public MessageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        MessageDTO dto = new MessageDTO();
                        dto.setMessageId(rs.getString("message_id"));
                        dto.setMessageContent(rs.getString("message_content"));
                        dto.setMessageTarget(rs.getString("message_target"));
                        dto.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                        return dto;
                    }
                }
        );
    }
}
