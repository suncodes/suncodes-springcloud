package eureka.client.provider.hystrix.command;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 信号量隔离采用的Web容器的线程池，而线程池隔离采用的是自己独立的线程池。
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThreadPoolHystrixCommand extends HystrixCommand<String> {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolHystrixCommand.class);
    private String poolName;

    public ThreadPoolHystrixCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ThreadPoolGroup"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(2)
                        .withKeepAliveTimeMinutes(5)
                        .withMaxQueueSize(2)
                        .withQueueSizeRejectionThreshold(10))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                ));
    }

    @Override
    protected String run() throws Exception {
        logger.info(poolName + ":我伤心我无奈,可是我默默等待");
        TimeUnit.MILLISECONDS.sleep(100);
        return poolName + "-run:缘分就是一生的等待";
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
}