package wang.ismy.push.client;

/**
 * @author MY
 * @date 2020/6/30 15:01
 */
public interface MessageHandler {

    /**
     * 接收到服务端消息回调
     * @param message
     */
    void handle(String message);
}
