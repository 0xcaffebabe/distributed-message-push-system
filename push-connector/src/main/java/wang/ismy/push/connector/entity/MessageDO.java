package wang.ismy.push.connector.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MY
 * @date 2020/7/8 15:09
 */
@Data
public class MessageDO {
    private String messageId;
    private String messageContent;
    private LocalDateTime createTime;
    private String messageTarget;
}
