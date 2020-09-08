package wang.ismy.push.admin;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import static org.junit.jupiter.api.Assertions.*;

class MessageConfirmListenerTest {

    /**
     * 测试监听结果获取
     * @throws InterruptedException
     */
    @Test
    void confirm() throws InterruptedException {
        MessageConfirmListener listener = new MessageConfirmListener();

        CorrelationData correlationData = new CorrelationData();
        boolean ack = true;
        String cause = "no cause";

        var t = new Thread(()->{
            MessageConfirmListener.ConfirmResult result = null;
            try {
                result = listener.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(ack,result.ack);
            assertEquals(cause,result.cause);
            assertEquals(correlationData,correlationData);
        });
        t.start();

        listener.confirm(correlationData,ack,cause);
    }
}