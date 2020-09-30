package wang.ismy.push.connector;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wang.ismy.push.connector.entity.MessageDO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author MY
 * @date 2020/7/8 15:08
 */
@AllArgsConstructor
@Repository
public class MessageDao {
    private final JdbcTemplate jdbcTemplate;

    public List<MessageDO> getLast15MinutesMessage(){
        return jdbcTemplate.query("SELECT * FROM tb_message WHERE create_time >= CURRENT_TIMESTAMP - INTERVAL 15 MINUTE",
                (rs, rowNum) -> {
                    MessageDO messageDO = new MessageDO();
                    messageDO.setMessageId(rs.getString("message_id"));
                    messageDO.setMessageContent(rs.getString("message_content"));
                    messageDO.setMessageTarget(rs.getString("message_target"));
                    messageDO.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                    return messageDO;
                });
    }
}
