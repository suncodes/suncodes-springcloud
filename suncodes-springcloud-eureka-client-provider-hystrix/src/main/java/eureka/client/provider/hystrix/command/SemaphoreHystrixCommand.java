package eureka.client.provider.hystrix.command;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 信号量隔离就是hystrix的限流功能。虽然名字叫隔离，实际上它是通过信号量来实现的。
 * 而信号量说白了就是个计数器。计数器计算达到设定的阈值，直接就做异常处理。
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SemaphoreHystrixCommand extends HystrixCommand<String> {
    private static final Logger logger = LoggerFactory.getLogger(SemaphoreHystrixCommand.class);
    private String poolName;

    public SemaphoreHystrixCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SemaphorePoolGroup"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("SemaphorePoolPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(2)
                        .withKeepAliveTimeMinutes(5)
                        .withMaxQueueSize(2)
                        .withQueueSizeRejectionThreshold(10))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
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