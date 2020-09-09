package wang.ismy.push.client;

import java.util.concurrent.CountDownLatch;
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
        for (int i = 0; i < n; i++) {
            int finalI = i;
            new Thread(()->{
                BioClient client = new BioClient(finalI +"");
                client.setMessageHandler(s->{
                    if ("abc".equals(s)){
                        receives.incrementAndGet();
                        System.out.println(receives);
                        latch.countDown();
                    }
                });
                try {
                    client.connect();
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
