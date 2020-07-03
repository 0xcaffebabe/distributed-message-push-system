package wang.ismy.push.admin;

import lombok.ToString;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.Exchanger;

/**
 * @author MY
 * @date 2020/7/2 15:33
 */
public class MessageConfirmListener implements RabbitTemplate.ConfirmCallback {
    @ToString
    public static class ConfirmResult {
        public CorrelationData correlationData;
        public boolean ack;
        public String cause;
    }
    private Exchanger<ConfirmResult> confirmResultExchanger = new Exchanger<>();
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        ConfirmResult result = new ConfirmResult();
        result.correlationData = correlationData;
        result.ack = ack;
        result.cause = cause;
        try {
            confirmResultExchanger.exchange(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ConfirmResult await() throws InterruptedException {
        return confirmResultExchanger.exchange(null);
    }
}
