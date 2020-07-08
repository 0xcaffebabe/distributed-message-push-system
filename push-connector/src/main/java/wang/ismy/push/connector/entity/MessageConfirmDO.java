package wang.ismy.push.connector.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MY
 * @date 2020/7/8 15:21
 */
@Data
public class MessageConfirmDO {
    private String messageId;
    private String messageTarget;
    private LocalDateTime createTime;
}
