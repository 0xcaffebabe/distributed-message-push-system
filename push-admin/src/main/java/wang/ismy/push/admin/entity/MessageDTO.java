package wang.ismy.push.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private String messageId;
    private String messageTarget;
    private String messageContent;
    private Long arrivalCount;
    private LocalDateTime createTime;
}
