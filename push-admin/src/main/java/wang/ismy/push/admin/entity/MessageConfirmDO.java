package wang.ismy.push.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MY
 * @date 2020/9/22 19:58
 */
@Data
public class MessageConfirmDO {
    private String messageId;
    private String messageTarget;
    private LocalDateTime createTime;
}
