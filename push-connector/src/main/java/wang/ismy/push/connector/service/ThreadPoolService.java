package wang.ismy.push.connector.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author MY
 * @date 2020/6/29 15:53
 */
@Service
public class ThreadPoolService {
    private ExecutorService executorService = new ThreadPoolExecutor(12, 12,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    });

    public void submit(Runnable runnable){
        executorService.submit(runnable);
    }

}
