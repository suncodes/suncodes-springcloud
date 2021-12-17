package eureka.client.provider.sentinel;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import eureka.client.provider.sentinel.persistence.MysqlRefreshableDataSource;
import eureka.client.provider.sentinel.pojo.po.ResourceRoleQps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SentinelConfiguration {

    @Bean
    public MysqlRefreshableDataSource mysqlRefreshableDataSource(Converter converter) {
        return new MysqlRefreshableDataSource(converter);
    }

    @Bean
    public Converter converter() {
        return (Converter<List<ResourceRoleQps>, List<FlowRule>>) source ->
                source.stream().map(resourceRoleQps -> {
                    FlowRule flowRule = new FlowRule();
                    flowRule.setResource(resourceRoleQps.getApi());
                    flowRule.setCount(resourceRoleQps.getLimitQps());
                    flowRule.setLimitApp(resourceRoleQps.getAppId());
                    flowRule.setGrade(1);
                    flowRule.setStrategy(0);
                    flowRule.setControlBehavior(0);
                    return flowRule;
                }).collect(Collectors.toList());
    }
}