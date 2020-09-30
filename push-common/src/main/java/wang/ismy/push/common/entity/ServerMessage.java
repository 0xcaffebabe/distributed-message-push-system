package wang.ismy.push.common.entity;

import lombok.Data;
import wang.ismy.push.common.enums.ServerMessageTypeEnum;

import java.io.Serializable;

/**
 * 后台通过MQ推送给connector的消息
 * @author MY
 * @date 2020/6/16 21:02
 */
@Data
public class ServerMessage implements Serializable {
    private byte[] payload;
    private ServerMessageTypeEnum messageType;
    private String to;
}
