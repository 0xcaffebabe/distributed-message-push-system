package wang.ismy.push.common.enums;

/**
 * 后台通过MQ推送给connector的消息类型
 * @author MY
 * @date 2020/9/30 13:48
 */
public enum ServerMessageTypeEnum {

    /**
     * 此类型推送消息给一个具体的用户
     */
    SINGLE_MESSAGE_TYPE(0),


    /**
     * 此类型表示为一条广播消息
     */
    BROADCAST_MESSAGE_TYPE(1),


    /**
     * 此类型表示踢出某一用户
     */
    KICK_OUT_MESSAGE_TYPE(2);


    private int code;

    ServerMessageTypeEnum(int code) {
        this.code = code;
    }
}
