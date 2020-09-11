package wang.ismy.push.client.example;

import wang.ismy.push.client.BioClient;
import wang.ismy.push.client.Client;

import wang.ismy.push.client.factory.ClientFactory;
import wang.ismy.push.client.message.AutoConfirmMessageHandler;
import wang.ismy.push.common.entity.ClientMessage;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MY
 * @date 2020/6/30 14:54
 */
public class ConcurrentClientTest {


    public static void main(String[] args) throws InterruptedException {
        int n = 1000;
        AtomicInteger receives = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(n);
        CyclicBarrier barrier = new CyclicBarrier(n);
        for (int i = 0; i < n; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                Client client = ClientFactory.newBioClient(finalI +"");
                client.setMessageHandler(new AutoConfirmMessageHandler(client) {
                    @Override
                    public void handle0(ClientMessage message) {
                        if ("con-test".equals(message.getPayload())){
                            receives.incrementAndGet();
                            System.out.println("消息到达率:"+receives.get()*1.0/n*100+"%");
                            latch.countDown();
                        }
                    }
                });
                try {
                    client.connect(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        latch.await();
        System.out.println("消息到达率:"+receives.get()*1.0/n*100+"%");
        System.exit(0);
    }

}
