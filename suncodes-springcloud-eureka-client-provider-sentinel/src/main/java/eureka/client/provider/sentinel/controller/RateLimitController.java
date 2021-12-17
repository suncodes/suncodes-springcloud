package eureka.client.provider.sentinel.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import eureka.client.provider.sentinel.handler.CustomerBlockHandler;
import eureka.client.provider.sentinel.persistence.MysqlRefreshableDataSource;
import eureka.client.provider.sentinel.pojo.bo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class RateLimitController {

    @Autowired
    private MysqlRefreshableDataSource mysqlRefreshableDataSource;

    @PostConstruct
    public void init() {
        // 自定义拉取数据源
        FlowRuleManager.register2Property(mysqlRefreshableDataSource.getProperty());
    }

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult(200, "按资源名称限流测试OK");
    }

    public CommonResult handleException(BlockException exception) {
        return new CommonResult(444, exception.getClass().getCanonicalName() + "\t 服务不可用");
    }

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException")
    public CommonResult customerBlockHandler() {
        return new CommonResult(200, "按客戶自定义");
    }
}

