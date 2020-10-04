package wang.ismy.push.client.message;

import lombok.Data;

/**
 * 发送到客户端的消息
 * @author MY
 * @date 2020/7/1 15:20
 */
@Data
public class ClientMessage {
    private String messageId;
    private String messageType;
    private String payload;
}
