package wang.ismy.push.connector;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wang.ismy.push.connector.entity.MessageConfirmDO;
import wang.ismy.push.connector.entity.MessageDO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author MY
 * @date 2020/7/7 15:10
 */
@Repository
@AllArgsConstructor
public class MessageConfirmDao {
    private final JdbcTemplate jdbcTemplate;

    public int addMessageConfirm(String messageId,String clientId){
        return jdbcTemplate.update("INSERT INTO tb_message_confirm(message_id,message_target,create_time) VALUES(?,?,NOW())",messageId,clientId);
    }

    public List<MessageConfirmDO> getByMessageId(String messageId){
        return jdbcTemplate.query("SELECT * FROM tb_message_confirm WHERE message_id = ?",
                new RowMapper<MessageConfirmDO>() {
                    @Override
                    public MessageConfirmDO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        MessageConfirmDO MessageConfirmDO = new MessageConfirmDO();
                        MessageConfirmDO.setMessageId(rs.getString("message_id"));
                        MessageConfirmDO.setMessageTarget(rs.getString("message_target"));
                        MessageConfirmDO.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                        return MessageConfirmDO;
                    }
                },messageId);
    }
}
