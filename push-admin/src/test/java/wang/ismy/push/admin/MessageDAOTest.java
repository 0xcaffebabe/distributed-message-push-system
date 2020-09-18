package wang.ismy.push.admin;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import wang.ismy.push.common.entity.ClientMessage;
import wang.ismy.push.common.entity.Message;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class MessageDAOTest {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void addMessage() {
        Message message = new Message();
        message.setPayload("content".getBytes());
        message.setTo("cxk");

        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setMessageId("1");

        MessageConfirmListener.ConfirmResult result = new MessageConfirmListener.ConfirmResult();
        result.ack = true;

        int ret = messageDAO.addMessage(message,clientMessage,result);
        assertEquals(1,ret);

        jdbcTemplate.query("SELECT * FROM tb_message", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String messageId = rs.getString("message_id");
                String messageContent = rs.getString("message_content");
                boolean isSend = rs.getBoolean("is_send");
                String messageTarget = rs.getString("message_target");

                assertEquals(clientMessage.getMessageId(),messageId);
                assertEquals(new String(message.getPayload()),messageContent);
                assertEquals(result.ack,isSend);
                assertEquals(message.getTo(),messageTarget);
            }
        });
    }

    @Test
    @Transactional
    void findLimit10(){
        addMessage();

        var list = messageDAO.findLimit10();
        assertEquals(1,list.size());
    }
}