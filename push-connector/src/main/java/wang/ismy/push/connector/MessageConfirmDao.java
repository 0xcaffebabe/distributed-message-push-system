package wang.ismy.push.connector;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
