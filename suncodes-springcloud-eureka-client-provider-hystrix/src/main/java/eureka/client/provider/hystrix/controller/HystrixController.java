package eureka.client.provider.hystrix.controller;

import eureka.client.provider.hystrix.command.SemaphoreHystrixCommand;
import eureka.client.provider.hystrix.command.ThreadPoolHystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixController {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * hystrix 隔离策略 线程池
     * http://localhost:8004/hystrix-thread?poolName=t
     * http://localhost:8004/hystrix-thread?poolName=h
     *
     * @param poolName
     * @return
     * @throws Exception
     */
    @GetMapping("/hystrix-thread")
    public String hystrixThread(String poolName) throws Exception {
        ThreadPoolHystrixCommand threadPoolHystrixCommand =
                applicationContext.getBean(ThreadPoolHystrixCommand.class);
        threadPoolHystrixCommand.setPoolName(poolName);
        return threadPoolHystrixCommand.execute();
    }

    /**
     * hystrix 隔离策略 信号量
     * http://localhost:8004/hystrix-semaphore?poolName=s
     * http://localhost:8004/hystrix-semaphore?poolName=e
     *
     * @param poolName
     * @return
     * @throws Exception
     */
    @GetMapping("/hystrix-semaphore")
    public String hystrixSemaphore(String poolName) throws Exception {
        SemaphoreHystrixCommand semaphoreHystrixCommand =
                applicationContext.getBean(SemaphoreHystrixCommand.class);
        semaphoreHystrixCommand.setPoolName(poolName);
        return semaphoreHystrixCommand.execute();
    }
}
